package com.delivery.util.updateData;

import com.delivery.dto.OrderRatingRequestDto;
import com.delivery.entity.Order;
import com.delivery.entity.OrderRating;
import org.springframework.stereotype.Component;

@Component
public class OrderRatingDataService {
    public OrderRating createOrderRating(Order order, OrderRatingRequestDto dto) {
        OrderRating rating = new OrderRating();
        rating.setOrder(order);
        rating.setClient(order.getClient());
        rating.setStars(dto.getStars());
        rating.setComment(dto.getComment());
        return rating;
    }

}
