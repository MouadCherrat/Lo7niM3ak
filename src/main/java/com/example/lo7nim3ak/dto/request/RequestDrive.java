package com.example.lo7nim3ak.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class RequestDrive {
    private String pickup;
    private String destination;
    private Date deptime;
    private BigDecimal price;
    private Integer seating;
    private String description;
    private Long driverId;

}
