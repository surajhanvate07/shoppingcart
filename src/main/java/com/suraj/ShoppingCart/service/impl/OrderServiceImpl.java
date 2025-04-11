package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.enums.OrderStatus;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Cart;
import com.suraj.ShoppingCart.model.Order;
import com.suraj.ShoppingCart.model.OrderItem;
import com.suraj.ShoppingCart.model.Product;
import com.suraj.ShoppingCart.repository.OrderRepository;
import com.suraj.ShoppingCart.repository.ProductRepository;
import com.suraj.ShoppingCart.service.CartService;
import com.suraj.ShoppingCart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final CartService cartService;

	@Override
	public Order getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	}

	@Override
	public List<Order> getOrdersByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	@Override
	public Order placeOrder(Long userId) {
		Cart cart = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItemsList = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItemsList));
		order.setTotalAmount(calculateTotalAmount(orderItemsList));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());

		return savedOrder;
	}

	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return order;
	}

	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return cart.getCartItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return new OrderItem(
					order,
					product,
					cartItem.getQuantity(),
					cartItem.getUnitPrice()
					);
		}).toList();
	}

	private BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList) {
		return orderItemsList.stream()
				.map(orderItem -> orderItem.getPrice()
						.multiply(new BigDecimal(orderItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
