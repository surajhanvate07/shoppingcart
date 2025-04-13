package com.suraj.ShoppingCart.data;

import com.suraj.ShoppingCart.model.Role;
import com.suraj.ShoppingCart.model.User;
import com.suraj.ShoppingCart.repository.RoleRepository;
import com.suraj.ShoppingCart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_USER");
		createDefaultUserIfNotExists();
		createDefaultRolesIfNotExists(roles);
		createDefaultAdminIfNotExists();
	}

	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER").get();

		for (int i = 6; i <= 7; i++) {
			String defaultEmail = "user" + i + "@email.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("User" + i);
			user.setLastName("LastName" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("password" + i));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
		}
	}

	private void createDefaultAdminIfNotExists() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
		for (int i = 1; i <= 2; i++) {
			String defaultEmail = "admin" + i + "@email.com";
			if (userRepository.existsByEmail(defaultEmail)) {
				continue;
			}
			User user = new User();
			user.setFirstName("Admin" + i);
			user.setLastName("Admin LastName" + i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("password" + i));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Default Admin " + i + " created successfully with email: " + user.getEmail());
		}
	}

	private void createDefaultRolesIfNotExists(Set<String> roles) {
		roles.stream()
				.filter(role -> roleRepository.findByName(role).isEmpty())
				.map(Role::new).forEach(roleRepository::save);
	}
}
