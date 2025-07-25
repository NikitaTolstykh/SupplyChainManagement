package com.delivery.util.updateData;

import com.delivery.dto.OrderRequestDto;
import com.delivery.entity.Order;
import com.delivery.entity.User;
import com.delivery.service.interfaces.PriceCalculatorService;
import com.delivery.util.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderDataService {
    private final PriceCalculatorService priceCalculatorService;

    public OrderDataService(PriceCalculatorService priceCalculatorService) {
        this.priceCalculatorService = priceCalculatorService;
    }

    public void updateOrderFields(Order order, OrderRequestDto dto) {
        order.setFromAddress(dto.getFromAddress());
        order.setToAddress(dto.getToAddress());
        order.setCargoType(dto.getCargoType());
        order.setCargoDescription(dto.getCargoDescription());
        order.setWeightKg(dto.getWeightKg());
        order.setComment(dto.getComment());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setPickupTime(dto.getPickupTime());

        BigDecimal newPrice = priceCalculatorService.calculatePrice(dto.getWeightKg(), dto.getDistanceCategory());
        order.setPrice(newPrice);
    }

    public void assignDriverToOrder(Order order, User driver) {
        order.setDriver(driver);
        order.setVehicle(driver.getVehicle());
        changeStatusToAssigned(order);
    }

    public void changeStatusToAssigned(Order order) {
        if (order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.ASSIGNED);
        }
    }
    public void changeOrderStatus(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
    }

    public void cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
    }
}
