package com.servesync.service.auth;

import com.servesync.dto.auth.AuthResponseDTO;
import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.user.UserResponseDTO;
import com.servesync.entity.role.Role;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import com.servesync.exception.EmailAlreadyExistsException;
import com.servesync.repository.role.RoleRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.CustomUserDetails;
import com.servesync.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    /**
     * Registers a new user with default CUSTOMER role.
     */
    @Override
    public UserResponseDTO register(RegisterRequestDTO dto) {
        final String email = dto.getEmail();
        log.info("Registering user with email: {}", email);

        if (userRepository.existsByEmail(email)) {
            log.warn("Email already registered: {}", email);
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role customerRole = roleRepository.findByRoleName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> {
                    log.error("Default role {} not found", RoleName.ROLE_CUSTOMER);
                    return new IllegalStateException("Default user role not found in the database");
                });

        user.addRole(customerRole);

        try {
            User savedUser = userRepository.save(user);
            log.info("User registered successfully: id={}, email={}", savedUser.getId(), savedUser.getEmail());
            return modelMapper.map(savedUser, UserResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed - data integrity violation: {}", e.getMessage());
            throw new EmailAlreadyExistsException("Email is already registered");
        }
    }

    /**
     * Authenticates user and generates JWT token.
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        final String email = dto.getEmail();
        log.info("Logging in user with email: {}", email);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, dto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            log.info("Login successful for user: {}", email);

            return AuthResponseDTO.builder()
                    .token(jwt)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .roles(userDetails.getAuthorities().stream()
                            .map(Object::toString)
                            .collect(Collectors.toList()))
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for email: {}", email);
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            log.error("Login failed for email: {} due to: {}", email, e.getMessage());
            throw new RuntimeException("Login failed due to an unexpected error");
        }
    }
}
