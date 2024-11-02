package com.example.lo7nim3ak.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class ResponseDrive {
    private Long id;
    private String pickup;
    private String destination;
    private Date deptime;
    private BigDecimal price;
    private Integer seating;
    private String description;
    private Long driverId;

}
