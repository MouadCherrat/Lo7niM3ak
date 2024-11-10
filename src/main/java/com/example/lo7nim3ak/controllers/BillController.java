package com.example.lo7nim3ak.controllers;

import com.example.lo7nim3ak.entities.Bill;
import com.example.lo7nim3ak.entities.PaymentInfo;
import com.example.lo7nim3ak.services.BillService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;
    @GetMapping
    public ResponseEntity<List<Bill>> getAll(){
        return ResponseEntity.ok(billService.getAll());
    }

    @PostMapping("/create-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        PaymentIntent paymentIntent = billService.createPaymentIntent(paymentInfo);
        String paymentStr = paymentIntent.toJson();
        return ResponseEntity.ok(paymentStr);
    }
    @PostMapping
    public ResponseEntity<String> payBill(@RequestBody  Bill bill) {
        String response = billService.payBill(bill);
        return ResponseEntity.ok(response);
    }

}
