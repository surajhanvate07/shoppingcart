package com.suraj.ShoppingCart.dto;

import lombok.Data;

@Data
public class UserCreateDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
}
