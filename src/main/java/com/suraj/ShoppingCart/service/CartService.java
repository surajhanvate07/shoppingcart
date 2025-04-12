package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.model.Cart;
import com.suraj.ShoppingCart.model.User;

import java.math.BigDecimal;

public interface CartService {
	Cart getCart(Long cartId);
	void clearCart(Long cartId);
	BigDecimal getTotalPrice(Long cartId);

	Cart generateCart(User user);

	Cart getCartByUserId(Long userId);
}
