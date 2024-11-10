package com.example.lo7nim3ak.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder

public class BillRequest {

    private Long clientId;

    private Long reservationId;

    private BigDecimal amount;
}