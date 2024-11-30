package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.dto.ReservationDto;
import com.example.lo7nim3ak.entities.Bill;
import com.example.lo7nim3ak.entities.PaymentInfo;
import com.example.lo7nim3ak.entities.Reservation;
import com.example.lo7nim3ak.repository.BillRepository;
import com.example.lo7nim3ak.repository.ReservationRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final ReservationRepository reservationRepository;
    @Value("${stripe.key.secret}")
    private String secretKey;


    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        Stripe.apiKey = secretKey;

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Product purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }
    public List<Bill> getAll(){
        return billRepository.findAll();
    }
    public String payBill(Bill bill){
        billRepository.save(bill);
        return " Payment succeed";
    }
    public Bill createBillForReservation(ReservationDto reservationDto) {
        if (reservationDto.getId() == null) {
            throw new IllegalArgumentException("Reservation ID cannot be null");
        }

        Reservation reservation = reservationRepository.findById(reservationDto.getId())
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationDto.getId()));

        Bill bill = new Bill();
        bill.setTotalAmount(BigDecimal.valueOf(reservation.getSeats())
                .multiply(reservation.getDrive().getPrice())
                .setScale(2, RoundingMode.HALF_UP));
        bill.setPaid(false);
        bill.setBillReference(UUID.randomUUID().toString());
        bill.setReservation(reservation);

        return billRepository.save(bill);
    }



}
