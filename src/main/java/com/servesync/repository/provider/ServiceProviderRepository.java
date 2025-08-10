package com.servesync.repository.provider;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.provider.ServiceProvider;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    boolean existsByUserId(Long userId);
    Optional<ServiceProvider> findByUserId(Long userId);
}

