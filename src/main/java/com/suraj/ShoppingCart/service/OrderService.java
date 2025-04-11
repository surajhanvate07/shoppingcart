package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.dto.OrderDto;
import com.suraj.ShoppingCart.model.Order;

import java.util.List;

public interface OrderService {
	List<OrderDto> getOrdersByUserId(Long userId);

	OrderDto placeOrder(Long userId);
	OrderDto getOrder(Long orderId);
}
