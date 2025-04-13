package com.suraj.ShoppingCart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

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
