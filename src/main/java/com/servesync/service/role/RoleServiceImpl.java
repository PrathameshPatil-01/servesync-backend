package com.servesync.service.role;

import com.servesync.dto.role.CreateRoleRequestDTO;
import com.servesync.dto.role.RoleDTO;
import com.servesync.entity.role.Role;
import com.servesync.enums.RoleName;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.role.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(CreateRoleRequestDTO dto) {
        RoleName roleName = RoleName.valueOf(dto.getRoleName().toUpperCase());

        if (roleRepository.existsByRoleName(roleName)) {
            throw new IllegalArgumentException("Role already exists.");
        }

        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(dto.getDescription());

        Role saved = roleRepository.save(role);
        return modelMapper.map(saved, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Short id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public void deleteRole(Short id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}
