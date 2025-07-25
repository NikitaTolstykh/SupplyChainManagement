package com.delivery.service.impl;

import com.delivery.service.interfaces.PriceCalculatorService;
import com.delivery.util.DistanceCategory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceCalculatorServiceImpl implements PriceCalculatorService {
    private static final BigDecimal BASE_PRICE = new BigDecimal("20.00");
    private static final BigDecimal WEIGHT_COEFFICIENT = new BigDecimal("0.5");

    @Override
    public BigDecimal calculatePrice(BigDecimal weightKg, DistanceCategory distanceCategory) {
        BigDecimal distanceMultiplier = switch (distanceCategory) {
            case SHORT -> new BigDecimal("1.0");
            case MEDIUM -> new BigDecimal("1.5");
            case LONG -> new BigDecimal("2.0");
        };

        BigDecimal price = BASE_PRICE
                .add(weightKg.multiply(WEIGHT_COEFFICIENT))
                .multiply(distanceMultiplier);

        return price.setScale(2, RoundingMode.HALF_UP);
    }
}
