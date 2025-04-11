package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.model.Order;

import java.util.List;

public interface OrderService {
	List<Order> getOrdersByUserId(Long userId);

	Order placeOrder(Long userId);
	Order getOrder(Long orderId);
}
