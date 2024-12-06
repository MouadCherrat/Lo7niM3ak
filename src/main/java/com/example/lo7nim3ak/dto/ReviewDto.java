package com.example.lo7nim3ak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long userId;
    private String clientName;
}
