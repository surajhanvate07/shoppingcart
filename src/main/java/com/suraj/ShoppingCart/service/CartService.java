package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.model.Cart;

import java.math.BigDecimal;

public interface CartService {
	Cart getCart(Long cartId);
	void clearCart(Long cartId);
	BigDecimal getTotalPrice(Long cartId);

	Long generateCartId();

	Cart getCartByUserId(Long userId);
}
