package com.servesync.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.user.Role;
import com.servesync.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Short> {
    Optional<Role> findByRoleName(RoleName roleName);
}

