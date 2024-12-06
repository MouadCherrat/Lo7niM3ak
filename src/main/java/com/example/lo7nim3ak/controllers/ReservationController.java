package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.services.ReservationService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation reservation = reservationService.createReservation(reservationDto);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/{reservationId}/payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@PathVariable Long reservationId) throws StripeException {
        Map<String, String> response = reservationService.createPaymentIntent(reservationId);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/{reservationId}/confirm-payment")
//    public ResponseEntity<Map<String, String>> confirmPayment(
//            @PathVariable Long reservationId,
//            @RequestParam String paymentIntentId) throws StripeException {
//        String result = reservationService.confirmPayment(paymentIntentId, reservationId);
//
//        // Retournez une réponse JSON
//        Map<String, String> response = new HashMap<>();
//        response.put("message", result);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByUserId(@PathVariable Long userId) {
        List<ReservationDto> reservations = reservationService.getReservationsByUserId(userId);
        // Map the Reservation entities to ReservationDto objects
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> new ReservationDto(
                        reservation.getId(),
                        reservation.getSeats(),
                        reservation.getDriveId(),
                        reservation.getUserId(),
                        reservation.getStatus()
                ))
                .collect(Collectors.toList());

        if (reservationDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservationDtos);
    }

    @PutMapping("/{reservationId}/accept")
    public ResponseEntity<String> acceptReservation(@PathVariable Long reservationId) {
        try {
            reservationService.acceptReservation(reservationId);
            return ResponseEntity.ok("Reservation accepted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{reservationId}/refuse")
    public ResponseEntity<String> refuseReservation(@PathVariable Long reservationId) {
        try {
            reservationService.refuseReservation(reservationId);
            return ResponseEntity.ok("Reservation refused successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok("Reservation canceled successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/drive/{driveId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByDriveId(@PathVariable Long driveId) {
        List<ReservationDto> reservations = reservationService.getReservationsByDriveId(driveId);
        return reservations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reservations);}

}
