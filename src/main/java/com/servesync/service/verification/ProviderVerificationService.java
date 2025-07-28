package com.servesync.service.verification;


public interface ProviderVerificationService {
    List<ProviderVerificationResponse> getVerificationsByProvider(Long providerId);
    ProviderVerificationResponse submitVerification(Long providerId, ProviderVerificationRequest dto);
    void updateVerificationStatus(Long id, String status);
    void deleteVerification(Long id);
}
