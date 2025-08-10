package com.servesync.service.user;

import com.servesync.dto.role.RoleDTO;
import com.servesync.dto.user.UserCreateDTO;
import com.servesync.dto.user.UserResponseDTO;
import com.servesync.entity.role.Role;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.role.RoleRepository;
import com.servesync.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper; // Import ModelMapper
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper

    @Override
    public UserResponseDTO createUser(UserCreateDTO dto) {
        log.info("Creating user with email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        User user = modelMapper.map(dto, User.class);
        user.setRoles(resolveRoles(dto.getRoleIds()));

        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());

        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return mapToUserResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted with ID: {}", id);
    }

    // --- Utility Methods ---

    private Set<Role> resolveRoles(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            // Assign default CUSTOMER role
            Role defaultRole = roleRepository.findByRoleName(RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
            return Set.of(defaultRole);
        }

        return roleIds.stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id)))
                .collect(Collectors.toSet());
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);

        // Explicitly map roles as ModelMapper might struggle with Set<Role> to Set<RoleDTO>
        Set<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toSet());

        dto.setRoles(roleDTOs);
        return dto;
    }
}
