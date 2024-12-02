package com.example.lo7nim3ak.dto;

import com.example.lo7nim3ak.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationDto {
    private Long id;
    private int seats;
    private Long driveId;
    private Long userId;
    private Status status;
}
