package com.servesync.repository.provider;

import com.servesync.entity.provider.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    boolean existsByUserId(Long userId);
    Optional<Provider> findByUserId(Long userId);
}
