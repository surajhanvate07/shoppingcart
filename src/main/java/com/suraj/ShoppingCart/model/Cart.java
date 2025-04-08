package com.suraj.ShoppingCart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CartItem> cartItems = new HashSet<>();

	public void addItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
		cartItem.setCart(this);
		updateTotalAmount();
	}

	public void removeItem(CartItem cartItem) {
		this.cartItems.remove(cartItem);
		cartItem.setCart(null);
		updateTotalAmount();
	}

	public void updateTotalAmount() {
		this.totalAmount = cartItems.stream().map(cartItem -> {
			BigDecimal unitPrice = cartItem.getUnitPrice();
			if (unitPrice != null) {
				return unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
			} else {
				return BigDecimal.ZERO;
			}
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
