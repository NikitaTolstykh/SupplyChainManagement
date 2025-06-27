package com.delivery.service;

import com.delivery.dto.OrderRequestDto;
import com.delivery.util.DistanceCategory;

import java.math.BigDecimal;

public interface PriceCalculatorService {
    BigDecimal calculatePrice(BigDecimal weightKg, DistanceCategory distanceCategory);
}
