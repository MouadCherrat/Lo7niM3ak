package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.*;
import com.example.lo7nim3ak.repository.BillRepository;
import com.example.lo7nim3ak.repository.DriveRepository;
import com.example.lo7nim3ak.repository.ReservationRepository;
import com.example.lo7nim3ak.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final DriveRepository driveRepository;
    private final BillRepository billRepository;

    public Reservation createReservation(Reservation reservation) {
        User user = userRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));
        Drive drive = driveRepository.findById(reservation.getDrive().getId())
                .orElseThrow(() -> new IllegalArgumentException("Drive not found"));
        if (reservation.getSeats() > drive.getSeating() || reservation.getSeats() <= 0) {
            throw new IllegalArgumentException("Nombre de place incorrect!");
        }
        drive.setSeating(drive.getSeating()-reservation.getSeats());
        reservation.setUser(user);
        reservation.setDrive(drive);
        reservation.setStatus(Status.PENDING);
        return reservationRepository.save(reservation);
    }


    public Reservation acceptReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        Drive drive = reservation.getDrive();
        String ref = UUID.randomUUID().toString().substring(0, 5);
        Bill bill = Bill.builder()
                .totalAmount(drive.getPrice().multiply(BigDecimal.valueOf(reservation.getSeats())))
                .billReference("BILL-".concat(ref))
                .paid(false)
                .paymentMethod(PaymentMethod.COD)
                .reservation(reservation)
                .build();
        billRepository.save(bill);

        reservation.setStatus(Status.ACCEPTED);
        return reservationRepository.save(reservation);
    }
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        Drive drive = reservation.getDrive();
        drive.setSeating(drive.getSeating() + reservation.getSeats());
        driveRepository.save(drive);
        reservation.setStatus(Status.CANCELED);
        return reservationRepository.save(reservation);
    }
    public Reservation RefuseReservation (Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() == Status.REFUSED || reservation.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Reservation is already refused or cancelled");
        }

        Drive drive = reservation.getDrive();
        drive.setSeating(drive.getSeating() + reservation.getSeats());
        driveRepository.save(drive);
        reservation.setStatus(Status.REFUSED);
        return reservationRepository.save(reservation);
    }


}



