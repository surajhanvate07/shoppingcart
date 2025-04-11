package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.dto.OrderDto;
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
import org.modelmapper.ModelMapper;
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
	private final ModelMapper modelMapper;

	@Override
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.map(this::convertToDto)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	}

	@Override
	public List<OrderDto> getOrdersByUserId(Long userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		return orders.stream().map(this::convertToDto).toList();
	}

	@Override
	public OrderDto placeOrder(Long userId) {
		Cart cart = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItemsList = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItemsList));
		order.setTotalAmount(calculateTotalAmount(orderItemsList));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		return convertToDto(savedOrder);
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

	private OrderDto convertToDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}
}
