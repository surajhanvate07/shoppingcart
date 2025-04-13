package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.dto.UserCreateDto;
import com.suraj.ShoppingCart.dto.UserDto;
import com.suraj.ShoppingCart.dto.UserUpdateDto;
import com.suraj.ShoppingCart.exceptions.AlreadyExistsException;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.User;
import com.suraj.ShoppingCart.repository.UserRepository;
import com.suraj.ShoppingCart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	@Override
	public User createUser(UserCreateDto userDto) {
//		if (userRepository.existsByEmail(userDto.getEmail())) {
//			throw new ResourceNotFoundException("User already exists with email: " + userDto.getEmail());
//		}

		return Optional.of(userDto)
				.filter(user -> !userRepository.existsByEmail(userDto.getEmail()))
				.map(userDto1 -> {
					User user = new User();
					user.setFirstName(userDto1.getFirstName());
					user.setLastName(passwordEncoder.encode(userDto1.getLastName()));
					user.setEmail(userDto1.getEmail());
					user.setPassword(userDto1.getPassword());
					return userRepository.save(user);
				}).orElseThrow(() -> new AlreadyExistsException("User already exists with email: " + userDto.getEmail()));

	}

	@Override
	public User updateUser(UserUpdateDto userUpdateDto, Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		if (userUpdateDto.getFirstName() != null) {
			user.setFirstName(userUpdateDto.getFirstName());
		}
		if (userUpdateDto.getLastName() != null) {
			user.setLastName(userUpdateDto.getLastName());
		}
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
			throw new ResourceNotFoundException("User not found with id: " + userId);
		});
	}

	@Override
	public UserDto convertToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email);
	}
}
