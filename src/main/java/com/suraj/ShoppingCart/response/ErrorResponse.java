package com.suraj.ShoppingCart.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private int status;
	private String message;

	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
