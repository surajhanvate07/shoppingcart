package com.suraj.ShoppingCart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	public void setTotalPrice() {
		this.totalPrice = this.unitPrice.multiply(new BigDecimal(this.quantity));
	}
}
