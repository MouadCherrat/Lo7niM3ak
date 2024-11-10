package com.example.lo7nim3ak.controllers;


import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.Bill;
import com.example.lo7nim3ak.entities.PaymentInfo;
import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.services.BillService;
import com.example.lo7nim3ak.services.ReservationService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final BillService billService;

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

    @PostMapping("/pay-bill")
    public ResponseEntity<String> payBill(@RequestBody Bill bill) {
        String response = billService.payBill(bill);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create-payment-intent/{billId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@PathVariable Long billId) throws StripeException {
        Map<String, String> response = reservationService.createPaymentIntent(billId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestParam String paymentIntentId, @RequestParam Long billId) throws StripeException {
        String result = reservationService.confirmPayment(paymentIntentId, billId);
        return ResponseEntity.ok(result);
    }

}
