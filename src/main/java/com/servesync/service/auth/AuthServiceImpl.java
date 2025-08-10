package com.servesync.service.auth;

import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.LoginResponseDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.auth.RegisterResponseDTO;
import com.servesync.entity.role.Role;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import com.servesync.exception.EmailAlreadyExistsException;
import com.servesync.exception.UserRoleNotFoundException;
import com.servesync.repository.provider.ProviderRepository;
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
    private final ProviderRepository providerRepository; // Assuming this is needed for provider-specific logic
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    /**
     * Registers a new user with default CUSTOMER role.
     */
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        final String email = dto.getEmail();
        log.info("Registering user with email: {}", email);

        if (userRepository.existsByEmail(email)) {
            log.warn("Email already registered: {}", email);
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        RoleName requestedRole = dto.getRole() != null ? dto.getRole() : RoleName.ROLE_CUSTOMER;

        Role role = roleRepository.findByRoleName(requestedRole)
                .orElseThrow(() -> new UserRoleNotFoundException("User role not found in the database"));

        user.addRole(role);

        try {
            User savedUser = userRepository.save(user);
            log.info("User registered successfully: id={}, email={}", savedUser.getId(), savedUser.getEmail());
            return modelMapper.map(savedUser, RegisterResponseDTO.class);
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed - data integrity violation: {}", e.getMessage());
            throw new EmailAlreadyExistsException("Email is already registered");
        }
    }


    /**
     * Authenticates user and generates JWT token.
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
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

            LoginResponseDTO response = modelMapper.map(user, LoginResponseDTO.class);
            response.setToken(jwt);
            response.setUserId(user.getId());
            response.setRoles(userDetails.getAuthorities().stream()
                            .map(Object::toString)
                            .collect(Collectors.toList()));

            // If the user is a provider, include providerId in the response
            if (user.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
                providerRepository.findByUserId(user.getId()).ifPresent(provider -> {
                    response.setProviderId(provider.getId());
                });
            }


            return response;


        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for email: {}", email);
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            log.error("Login failed for email: {} due to: {}", email, e.getMessage());
            throw new RuntimeException("Login failed due to an unexpected error");
        }
    }
}
