package com.servesync.service.role;

import com.servesync.dto.role.CreateRoleRequestDTO;
import com.servesync.dto.role.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(CreateRoleRequestDTO dto);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Short id);
    void deleteRole(Short id);
}
