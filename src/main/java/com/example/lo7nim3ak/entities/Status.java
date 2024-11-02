package com.example.lo7nim3ak.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REFUSED("refused"),
    CANCELED("canceled"),
    CLOSED("closed");
    private final String Status;
}
