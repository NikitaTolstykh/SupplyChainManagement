package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRatingResponseDto {
    private Long id;
    private int stars;
    private String comment;
    private LocalDateTime createdAt;
    private Long orderId;
    private String clientFullName;
}
