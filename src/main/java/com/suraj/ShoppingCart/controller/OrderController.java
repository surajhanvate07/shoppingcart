package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.dto.OrderDto;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Order;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/order")
	public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
		try {
			OrderDto orderDto = orderService.placeOrder(userId);
			return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Failed to place order: " + e.getMessage(), INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/{orderId}/order")
	public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
		try {
			OrderDto orderDto = orderService.getOrder(orderId);
			return ResponseEntity.ok(new ApiResponse("Order retrieved successfully", orderDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Failed to retrieve order: " + e.getMessage(), INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("{userId}/orders")
	public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
		try {
			List<OrderDto> orders = orderService.getOrdersByUserId(userId);
			return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", orders));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Failed to retrieve orders: " + e.getMessage(), INTERNAL_SERVER_ERROR));
		}
	}
}
