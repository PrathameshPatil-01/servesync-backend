package com.servesync.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.user.RoleType;
import com.servesync.enums.RoleName;

import java.util.Optional;

public interface RoleTypeRepository extends JpaRepository<RoleType, Short> {
    Optional<RoleType> findByRoleName(RoleName roleName);
}

