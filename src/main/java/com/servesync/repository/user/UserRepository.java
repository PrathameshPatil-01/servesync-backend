package com.servesync.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}

