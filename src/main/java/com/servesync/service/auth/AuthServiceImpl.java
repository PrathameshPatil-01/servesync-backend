package com.servesync.service.auth;

import com.servesync.dto.auth.AuthResponseDTO;
import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.user.UserResponseDTO;
import com.servesync.entity.user.Role;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import com.servesync.exception.EmailAlreadyExistsException;
import com.servesync.repository.user.RoleRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.CustomUserDetails;
import com.servesync.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
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


    // Register method to create a new user
    @Override
    public UserResponseDTO register(RegisterRequestDTO dto) {

        log.info("Attempting to register user with email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Registration failed: Email {} already exists", dto.getEmail());
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role customerRole = roleRepository.findByRoleName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> {
                    log.error("Role {} not found in database", RoleName.ROLE_CUSTOMER);
                    return new IllegalStateException("Default role not found");
                });

        user.addRole(customerRole);

        try {
            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}", savedUser.getId());
            return modelMapper.map(savedUser, UserResponseDTO.class);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error("Registration failed due to database constraint: {}", e.getMessage());
            throw new EmailAlreadyExistsException("Email is already registered");
        }
    }



    // Login method to authenticate user and generate JWT token
    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        log.info("Attempting to login user with email: {}", dto.getEmail());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            // Set authentication in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            log.info("User {} logged in successfully", dto.getEmail());

            // Map to AuthResponseDTO
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return new AuthResponseDTO(
                    jwt,
                    userDetails.getUser().getId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(Object::toString)
                            .collect(Collectors.toList())
            );

        } catch (BadCredentialsException e) {
            log.warn("Login failed for email {}: Invalid credentials", dto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            log.error("Unexpected error during login for email {}: {}", dto.getEmail(), e.getMessage());
            throw new RuntimeException("Login failed due to an unexpected error");
        }
    }
}