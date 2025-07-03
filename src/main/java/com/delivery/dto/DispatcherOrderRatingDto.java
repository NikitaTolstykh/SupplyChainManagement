package com.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DispatcherOrderRatingDto {
    private Long id;
    private int stars;
    private String comment;
    private LocalDateTime createdAt;
    private Long orderId;
    private String clientFullName;
    private String driverFullName;
}
