package com.example.lo7nim3ak.services;

import com.example.lo7nim3ak.entities.Bill;
import com.example.lo7nim3ak.entities.PaymentInfo;
import com.example.lo7nim3ak.repository.BillRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
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


}
