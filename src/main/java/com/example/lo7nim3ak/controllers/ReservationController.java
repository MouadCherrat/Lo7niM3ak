package com.example.lo7nim3ak.controllers;


import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public Reservation addReservation(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @PostMapping("/{reservationId}/accept")
    public ResponseEntity<Reservation> acceptReservation(@PathVariable Long reservationId) {
        Reservation acceptedReservation = reservationService.acceptReservation(reservationId);
        return ResponseEntity.ok(acceptedReservation);
    }
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long reservationId) {
        Reservation canceledReservation = reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(canceledReservation);
    }
    @PostMapping("/{reservationId}/refuse")
    public ResponseEntity<Reservation> refuseReservation(@PathVariable Long reservationId) {
        Reservation refusedReservation = reservationService.RefuseReservation(reservationId);
        return ResponseEntity.ok(refusedReservation);
    }
}
