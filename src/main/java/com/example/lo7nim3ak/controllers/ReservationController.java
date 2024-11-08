package com.example.lo7nim3ak.controllers;


import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public Reservation addReservation(@RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    @PutMapping("/accept/{reservationId}")
    public Reservation acceptReservation(@PathVariable Long reservationId) {
        return reservationService.acceptReservation(reservationId);
    }

    @PutMapping("/refuse/{reservationId}")
    public Reservation refuseReservation(@PathVariable Long reservationId) {
        return reservationService.refuseReservation(reservationId);
    }

    @PutMapping("/cancel/{reservationId}")
    public Reservation cancelReservation(@PathVariable Long reservationId) {
        return reservationService.cancelReservation(reservationId);
    }
}
