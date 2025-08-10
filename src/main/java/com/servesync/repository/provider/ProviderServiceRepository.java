package com.servesync.repository.provider;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.provider.ProviderService;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {

}
