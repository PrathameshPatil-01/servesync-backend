package com.servesync.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.user.User;
import com.servesync.entity.user.UserRole;
import com.servesync.entity.user.UserRoleId;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser(User user);
}

