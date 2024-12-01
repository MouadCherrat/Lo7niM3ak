package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.services.ReservationService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/{reservationId}/confirm-payment")
    public ResponseEntity<Map<String, String>> confirmPayment(
            @PathVariable Long reservationId,
            @RequestParam String paymentIntentId) throws StripeException {
        String result = reservationService.confirmPayment(paymentIntentId, reservationId);

        // Retournez une réponse JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        return ResponseEntity.ok(response);
    }

}
