package com.suraj.ShoppingCart.exceptions;

import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.response.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Resource not found",
				new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiResponse> handleProductNotFoundException(ProductNotFoundException ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Product not found",
				new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ApiResponse> handleAlreadyExistsException(AlreadyExistsException ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Already exists",
				new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Invalid argument",
				new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleException(Exception ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Internal server error",
				new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
		ApiResponse errorResponse = new ApiResponse(
				"Internal server error",
				new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage())
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
