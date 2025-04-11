package com.suraj.ShoppingCart.repository;

import com.suraj.ShoppingCart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUserId(Long userId);

}
