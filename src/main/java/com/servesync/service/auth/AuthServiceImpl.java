// ✅ AuthServiceImpl.java
package com.servesync.service.auth;

import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.auth.AuthResponseDTO;
import com.servesync.dto.user.UserResponseDTO;
import com.servesync.entity.user.RoleType;
import com.servesync.entity.user.UserRole;
import com.servesync.enums.RoleName;
import com.servesync.repository.user.RoleTypeRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RoleTypeRepository roleTypeRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public UserResponseDTO register(RegisterRequestDTO dto) {
		userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
			throw new RuntimeException("Email already registered");
		});

		com.servesync.entity.user.User user = new com.servesync.entity.user.User();
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setPhoneCountryCode("+91");
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setIsDeleted(false);

		RoleType customerRole = roleTypeRepository.findByRoleName(RoleName.CUSTOMER)
				.orElseThrow(() -> new RuntimeException("Role not found: CUSTOMER"));

		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRoleType(customerRole);
		userRole.setAssignedAt(LocalDateTime.now());

		user.getUserRoles().add(userRole);
		com.servesync.entity.user.User savedUser = userRepository.save(user);

		return new UserResponseDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(),
				savedUser.getEmail(), savedUser.getPhoneNumber(), customerRole.getRoleName().name());
	}

	@Override
	public AuthResponseDTO login(LoginRequestDTO dto) {
		com.servesync.entity.user.User user = userRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect password");
		}

		String role = user.getUserRoles().stream().map(ur -> ur.getRoleType().getRoleName().name()).findFirst()
				.orElse(RoleName.CUSTOMER.name());

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), List.of(() -> "ROLE_" + role));

		String token = jwtUtil.generateToken(userDetails);

		return new AuthResponseDTO(token, role);
	}
}
