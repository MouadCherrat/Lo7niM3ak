package com.example.lo7nim3ak.entities;

import lombok.Data;

@Data
public class PaymentInfo {

    private int amount;
    private String currency;
    private String receiptEmail;

}
