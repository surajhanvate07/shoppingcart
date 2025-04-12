package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.dto.UserCreateDto;
import com.suraj.ShoppingCart.dto.UserDto;
import com.suraj.ShoppingCart.dto.UserUpdateDto;
import com.suraj.ShoppingCart.model.User;

public interface UserService {
	User getUserById(Long userId);
	User createUser(UserCreateDto userDto);
	User updateUser(UserUpdateDto userUpdateDto, Long userId);
	void deleteUser(Long userId);

	UserDto convertToDto(User user);
}
