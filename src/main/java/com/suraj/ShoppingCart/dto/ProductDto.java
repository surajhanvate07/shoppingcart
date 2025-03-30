package com.suraj.ShoppingCart.dto;

import com.suraj.ShoppingCart.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
	private Long id;
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private String description;
	private Category category;
}
