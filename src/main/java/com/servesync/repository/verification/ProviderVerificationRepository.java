package com.servesync.repository.verification;

import com.servesync.entity.verification.ProviderVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderVerificationRepository extends JpaRepository<ProviderVerification, Long> {
}
