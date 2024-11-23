package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.*;
import com.example.lo7nim3ak.repository.BillRepository;
import com.example.lo7nim3ak.repository.DriveRepository;
import com.example.lo7nim3ak.repository.ReservationRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final DriveRepository driveRepository;
    private final BillRepository billRepository;
    private final NotificationService notificationService;
    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    public Reservation createReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        Drive drive = driveRepository.findById(reservationDto.getDriveId())
                .orElseThrow(() -> new IllegalArgumentException("Drive not found"));

        if (reservationDto.getSeats() > drive.getSeating() || reservationDto.getSeats() <= 0) {
            throw new IllegalArgumentException("Invalid number of seats!");
        }

        Reservation reservation = Reservation.builder()
                .seats(reservationDto.getSeats())
                .user(user)
                .drive(drive)
                .status(Status.PENDING)
                .build();

        reservationRepository.save(reservation);

        // Send email to the driver when the reservation is created
        notificationService.sendEmail(
                drive.getUser().getEmail(),
                "New Reservation Created",
                "A new reservation has been created with " + reservation.getSeats() + " seats reserved."
        );

        return reservation;
    }
    public String payBill(Bill bill){
        billRepository.save(bill);
        return " Payment succeed";
    }

    public Reservation acceptReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        Drive drive = reservation.getDrive();

        if (reservation.getSeats() > drive.getSeating()) {
            throw new IllegalArgumentException("Insufficient seats available in the drive for this reservation");
        }

        drive.setSeating(drive.getSeating() - reservation.getSeats());
        driveRepository.save(drive);

        String ref = UUID.randomUUID().toString().substring(0, 5);
        Bill bill = Bill.builder()
                .totalAmount(drive.getPrice().multiply(BigDecimal.valueOf(reservation.getSeats())))
                .billReference("BILL-".concat(ref))
                .paid(false)
                .paymentMethod(PaymentMethod.STRIPE)
                .reservation(reservation)
                .build();
        billRepository.save(bill);

        reservation.setStatus(Status.ACCEPTED);
        reservationRepository.save(reservation);

        // Send email to the passenger when reservation is accepted
        notificationService.sendEmail(
                reservation.getUser().getEmail(),
                "Reservation Accepted",
                "Your reservation has been accepted."
        );

        return reservation;
    }

    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() == Status.REFUSED || reservation.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Reservation is already refused or cancelled");
        }

        Drive drive = reservation.getDrive();
        drive.setSeating(drive.getSeating() + reservation.getSeats());
        driveRepository.save(drive);

        reservation.setStatus(Status.CANCELED);
        reservationRepository.save(reservation);

        // Send email to the driver when reservation is canceled
        notificationService.sendEmail(
                drive.getUser().getEmail(),
                "Reservation Canceled",
                "A reservation has been canceled. The seats have been restored."
        );

        return reservation;
    }

    public Reservation refuseReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() == Status.REFUSED || reservation.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Reservation is already refused or cancelled");
        }

        Drive drive = reservation.getDrive();
        drive.setSeating(drive.getSeating() + reservation.getSeats());
        driveRepository.save(drive);

        reservation.setStatus(Status.REFUSED);
        reservationRepository.save(reservation);

        // Send email to the passenger when reservation is refused
        notificationService.sendEmail(
                reservation.getUser().getEmail(),
                "Reservation Refused",
                "Your reservation has been refused."
        );

        return reservation;
    }
    public Map<String, String> createPaymentIntent(Long billId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));

        Map<String, Object> params = new HashMap<>();
        params.put("amount", bill.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue()); // Convert to cents
        params.put("currency", "usd");
        params.put("description", "Payment for Reservation ID: " + bill.getReservation().getId());
        params.put("payment_method_types", List.of("card"));

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("id", paymentIntent.getId());
        response.put("client_secret", paymentIntent.getClientSecret());

        return response;
    }


    public String confirmPayment(String paymentIntentId, Long billId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        if ("succeeded".equals(paymentIntent.getStatus())) {
            Bill bill = billRepository.findById(billId)
                    .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
            bill.setPaid(true);
            billRepository.save(bill);

            // Update reservation status to COMPLETED if desired
            Reservation reservation = bill.getReservation();
            reservation.setStatus(Status.CLOSED);
            reservationRepository.save(reservation);

            return "Payment succeeded, bill updated";
        } else {
            throw new RuntimeException("Payment not successful");
        }
    }

}