package com.suraj.ShoppingCart.security.jwt;

import com.suraj.ShoppingCart.security.user.ShopUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private int expirationTime;

	public String generateTokenForUser(Authentication authentication) {
		ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

		List<String> roles = userPrincipal.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		// For 0.12.0 JWT version
		return Jwts.builder()
				.subject(userPrincipal.getEmail())
				.claim("id", userPrincipal.getId())
				.claim("roles", roles)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey(), Jwts.SIG.HS256)
				.compact();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String getUserNameFromToken(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtException(e.getMessage());
		}
	}

}
