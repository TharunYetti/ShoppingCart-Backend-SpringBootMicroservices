package com.tharunyetti.OrderService.service;

import com.tharunyetti.OrderService.model.OrderRequest;
import com.tharunyetti.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
