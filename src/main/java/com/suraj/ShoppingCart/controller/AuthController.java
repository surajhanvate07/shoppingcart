package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.dto.LoginRequestDto;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.response.JwtResponse;
import com.suraj.ShoppingCart.security.jwt.JwtUtils;
import com.suraj.ShoppingCart.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateTokenForUser(authentication);
			ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
			JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

			return ResponseEntity.ok(new ApiResponse("Login successful", jwtResponse));
		} catch (AuthenticationException e) {
			return new ResponseEntity<>(new ApiResponse(e.getMessage(), null), HttpStatus.UNAUTHORIZED);
		}
	}
}
