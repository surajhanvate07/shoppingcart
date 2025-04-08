package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Cart;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {

	private final CartService cartService;

	@GetMapping("/{cartId}/my-cart")
	public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
		try {
			Cart cart = cartService.getCart(cartId);
			return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully", cart));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@DeleteMapping("/{cartId}/clear")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
		try {
			cartService.clearCart(cartId);
			return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/{cartId}/total-amount")
	public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
		try {
			BigDecimal totalPrice = cartService.getTotalPrice(cartId);
			return ResponseEntity.ok(new ApiResponse("Total amount retrieved successfully", totalPrice));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
