package com.example.lo7nim3ak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Date timestamp;
    private Boolean isRead;
}
