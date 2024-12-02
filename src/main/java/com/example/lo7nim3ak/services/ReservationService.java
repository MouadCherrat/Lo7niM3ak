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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BillRepository billRepository;
    private final DriveRepository driveRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    public Reservation createReservation(ReservationDto reservationDto) {
        // Validate user and drive
        User user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        Drive drive = driveRepository.findById(reservationDto.getDriveId())
                .orElseThrow(() -> new IllegalArgumentException("Drive not found"));

        if (reservationDto.getSeats() > drive.getSeating() || reservationDto.getSeats() <= 0) {
            throw new IllegalArgumentException("Invalid number of seats!");
        }
        // Create reservation
        Reservation reservation = Reservation.builder()
                .seats(reservationDto.getSeats())
                .user(user)
                .drive(drive)
                .status(Status.PENDING)
                .build();
        reservationRepository.save(reservation);

        String driverEmail = drive.getUser().getEmail();
        notificationService.sendEmail(
                driverEmail,
                "New Reservation Created",
                String.format("A new reservation has been created by %s for your drive from %s to %s.",
                        user.getFirstName(),
                        drive.getPickup(),
                        drive.getDestination())
        );
        return reservation;
    }

    public Map<String, String> createPaymentIntent(Long reservationId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Bill bill = billRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found for reservation ID: " + reservationId));
        Map<String, Object> params = new HashMap<>();
        params.put("amount", bill.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue()); // Convert to cents
        params.put("currency", "usd");
        params.put("description", "Payment for Reservation ID: " + reservationId);
        params.put("payment_method_types", List.of("card"));
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        Map<String, String> response = new HashMap<>();
        response.put("client_secret", paymentIntent.getClientSecret());
        response.put("payment_intent_id", paymentIntent.getId());
        return response;
    }

    public String confirmPayment(String paymentIntentId, Long reservationId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        if ("succeeded".equals(paymentIntent.getStatus())) {
            Bill bill = billRepository.findByReservationId(reservationId)
                    .orElseThrow(() -> new IllegalArgumentException("Bill not found for reservation ID: " + reservationId));
            // Mark bill as paid
            bill.setPaid(true);
            billRepository.save(bill);

            // Update reservation status
            Reservation reservation = bill.getReservation();
            reservation.setStatus(Status.CLOSED);
            reservationRepository.save(reservation);

            return "Payment confirmed, reservation updated to CONFIRMED.";
        } else {
            throw new RuntimeException("Payment not successful.");
        }
    }

    public List<ReservationDto> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .id(reservation.getId())
                        .seats(reservation.getSeats())
                        .driveId(reservation.getDrive().getId())
                        .userId(reservation.getUser().getId())
                        .status(reservation.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public void acceptReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Only PENDING reservations can be accepted");
        }
        reservation.setStatus(Status.ACCEPTED);
        Drive drive = reservation.getDrive();
        if (drive.getSeating() < reservation.getSeats()) {
            throw new IllegalArgumentException("Not enough available seats for this reservation");
        }
        drive.setSeating(drive.getSeating() - reservation.getSeats());
        reservationRepository.save(reservation);
        driveRepository.save(drive);

        // Create bill for the reservation
        Bill bill = Bill.builder()
                .totalAmount(drive.getPrice().multiply(BigDecimal.valueOf(reservation.getSeats())))
                .billReference("BILL-" + UUID.randomUUID().toString().substring(0, 8))
                .paid(false)
                .paymentMethod(PaymentMethod.STRIPE)
                .reservation(reservation)
                .build();
        billRepository.save(bill);

        String clientEmail = reservation.getUser().getEmail();
        notificationService.sendEmail(
                clientEmail,
                "Reservation Accepted",
                String.format("Your reservation for the drive from %s to %s has been accepted by the driver.",
                        drive.getPickup(),
                        drive.getDestination())
        );
    }
    public void refuseReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Only PENDING reservations can be refused");
        }
        reservation.setStatus(Status.REFUSED);
        reservationRepository.save(reservation);
        String clientEmail = reservation.getUser().getEmail();
        notificationService.sendEmail(
                clientEmail,
                "Reservation Refused",
                String.format("Your reservation for the drive from %s to %s has been refused by the driver.",
                        reservation.getDrive().getPickup(),
                        reservation.getDrive().getDestination())
        );
    }
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(Status.CANCELED);
        reservationRepository.save(reservation);
        Drive drive = reservation.getDrive();
        String driverEmail = drive.getUser().getEmail();
        notificationService.sendEmail(
                driverEmail,
                "Reservation Canceled",
                String.format("Reservation by %s for your drive from %s to %s has been canceled.",
                        reservation.getUser().getFirstName(),
                        drive.getPickup(),
                        drive.getDestination())
        );

    }

    public List<ReservationDto> getReservationsByDriveId(Long driveId) {
        List<Reservation> reservations = reservationRepository.findByDriveId(driveId);
        return reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .id(reservation.getId())
                        .seats(reservation.getSeats())
                        .driveId(reservation.getDrive().getId())
                        .userId(reservation.getUser().getId())
                        .status(reservation.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

}
