package com.suraj.ShoppingCart.repository;

import com.suraj.ShoppingCart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	void deleteAllByCartId(Long cartId);
}
