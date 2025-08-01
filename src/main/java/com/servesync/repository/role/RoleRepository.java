package com.servesync.repository.role;

import com.servesync.entity.role.Role;
import com.servesync.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Short> {
    Optional<Role> findByRoleName(RoleName roleName);
    boolean existsByRoleName(RoleName roleName);
	Optional<Role> findById(Long id);
}


