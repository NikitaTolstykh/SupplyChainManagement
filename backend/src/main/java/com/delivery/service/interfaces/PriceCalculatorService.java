package com.delivery.service.interfaces;

import com.delivery.util.DistanceCategory;

import java.math.BigDecimal;

public interface PriceCalculatorService {
    BigDecimal calculatePrice(BigDecimal weightKg, DistanceCategory distanceCategory);
}
