package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Cart;
import com.suraj.ShoppingCart.repository.CartItemRepository;
import com.suraj.ShoppingCart.repository.CartRepository;
import com.suraj.ShoppingCart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final AtomicLong cartIdGenerator = new AtomicLong(0);

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));
		BigDecimal totalAmount = cart.getTotalAmount();
		cart.setTotalAmount(totalAmount);
		return cartRepository.save(cart);
	}

	@Override
	@Transactional
	public void clearCart(Long cartId) {
		Cart cart = getCart(cartId);
		cartItemRepository.deleteAllByCartId(cartId);
		cart.getCartItems().clear();
		cartRepository.deleteById(cartId);
	}

	@Override
	public BigDecimal getTotalPrice(Long cartId) {
		Cart cart = getCart(cartId);
		return cart.getTotalAmount();
	}

	@Override
	public Long generateCartId() {
		Cart cart = new Cart();
		return cartRepository.save(cart).getId();
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}
}
