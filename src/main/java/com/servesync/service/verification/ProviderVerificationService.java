package com.servesync.service.verification;

import com.servesync.dto.verification.ProviderVerificationRequestDTO;
import com.servesync.dto.verification.ProviderVerificationResponseDTO;

public interface ProviderVerificationService {

    ProviderVerificationResponseDTO submitVerification(ProviderVerificationRequestDTO dto);

    ProviderVerificationResponseDTO verifyProvider(Long verificationId, Long verifierUserId, boolean isApproved, String rejectionReason);
}
