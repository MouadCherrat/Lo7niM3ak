package com.example.lo7nim3ak.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {
    COD("Cod"),
    STRIPE("Stripe");

    private final String PaymentMethod;

}
