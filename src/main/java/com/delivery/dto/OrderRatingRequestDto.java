package com.delivery.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class OrderRatingRequestDto {
    @Min(1)
    @Max(5)
    private int stars;

    private String comment;
}
