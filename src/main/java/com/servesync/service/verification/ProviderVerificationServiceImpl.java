package com.servesync.service.verification;

import com.servesync.dto.verification.ProviderVerificationRequestDTO;
import com.servesync.dto.verification.ProviderVerificationResponseDTO;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.user.User;
import com.servesync.entity.verification.ProviderVerification;
import com.servesync.enums.VerificationStatusEnum;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ProviderRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.repository.verification.ProviderVerificationRepository;
import com.servesync.service.verification.ProviderVerificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderVerificationServiceImpl implements ProviderVerificationService {

    private final ProviderVerificationRepository verificationRepo;
    private final ProviderRepository providerRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProviderVerificationResponseDTO submitVerification(ProviderVerificationRequestDTO dto) {
        log.info("Submitting provider verification for providerId: {}", dto.getProviderId());

        Provider provider = providerRepo.findById(dto.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + dto.getProviderId()));

        ProviderVerification verification = new ProviderVerification();
        verification.setProvider(provider);
        verification.setDocumentType(dto.getDocumentType());
        verification.setDocumentNumber(dto.getDocumentNumber());
        verification.setDocumentHash(dto.getDocumentHash());
        verification.setFrontImageUrl(dto.getFrontImageUrl());
        verification.setBackImageUrl(dto.getBackImageUrl());
        verification.setStatus(VerificationStatusEnum.PENDING);
        verification.setSubmittedAt(LocalDateTime.now());

        ProviderVerification savedVerification = verificationRepo.save(verification);
        log.info("Provider verification submitted with ID: {}", savedVerification.getId());

        return modelMapper.map(savedVerification, ProviderVerificationResponseDTO.class);
    }

    @Override
    @Transactional
    public ProviderVerificationResponseDTO verifyProvider(Long verificationId, Long verifierUserId, boolean isApproved, String rejectionReason) {
        log.info("Processing verification. ID: {}, Approved: {}, Verifier ID: {}", verificationId, isApproved, verifierUserId);

        ProviderVerification verification = verificationRepo.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("ProviderVerification not found with ID: " + verificationId));

        User verifier = userRepo.findById(verifierUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User (verifier) not found with ID: " + verifierUserId));

        if (!isApproved && (rejectionReason == null || rejectionReason.trim().isEmpty())) {
            throw new IllegalArgumentException("Rejection reason must be provided for rejected verifications.");
        }

        verification.setStatus(isApproved ? VerificationStatusEnum.APPROVED : VerificationStatusEnum.REJECTED);
        verification.setVerifiedBy(verifier);
        verification.setVerifiedAt(LocalDateTime.now());
        verification.setRejectionReason(isApproved ? null : rejectionReason.trim());

        ProviderVerification updatedVerification = verificationRepo.save(verification);
        log.info("Verification ID: {} processed with status: {}", verificationId, updatedVerification.getStatus());

        return modelMapper.map(updatedVerification, ProviderVerificationResponseDTO.class);
    }

}
