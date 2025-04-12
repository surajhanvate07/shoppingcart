package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.dto.UserCreateDto;
import com.suraj.ShoppingCart.dto.UserDto;
import com.suraj.ShoppingCart.dto.UserUpdateDto;
import com.suraj.ShoppingCart.exceptions.AlreadyExistsException;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.User;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}/user")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		try {
			User user = userService.getUserById(userId);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.ok(new ApiResponse("User fetched successfully", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		}
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDto userCreateDto) {
		try {
			User user = userService.createUser(userCreateDto);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User created successfully", userDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
		}
	}

	@PutMapping("/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId) {
		try {
			User user = userService.updateUser(userUpdateDto, userId);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		}
	}

	@DeleteMapping("/{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		}
	}

}
