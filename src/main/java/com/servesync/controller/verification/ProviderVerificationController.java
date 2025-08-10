package com.servesync.controller.verification;

import com.servesync.dto.verification.ProviderVerificationRequestDTO;
import com.servesync.dto.verification.ProviderVerificationResponseDTO;
import com.servesync.service.verification.ProviderVerificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
@Tag(name = "Provider Verification", description = "Endpoints for provider document verification workflow")
public class ProviderVerificationController {

    private final ProviderVerificationService verificationService;

    @PostMapping("/submit")
    @Operation(summary = "Submit verification request for a provider")
    public ResponseEntity<ProviderVerificationResponseDTO> submitVerification(
            @Valid @RequestBody ProviderVerificationRequestDTO requestDTO) {
        return ResponseEntity.ok(verificationService.submitVerification(requestDTO));
    }

    @PostMapping("/{verificationId}/verify")
    @Operation(summary = "Approve or reject a provider verification")
    public ResponseEntity<ProviderVerificationResponseDTO> verifyProvider(
            @PathVariable Long verificationId,
            @RequestParam Long verifierUserId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String rejectionReason) {
        return ResponseEntity.ok(
                verificationService.verifyProvider(verificationId, verifierUserId, approved, rejectionReason));
    }

//    @GetMapping("/{verificationId}")
//    @Operation(summary = "Get verification details by ID")
//    public ResponseEntity<ProviderVerificationResponseDTO> getVerificationById(@PathVariable Long verificationId) {
//        return ResponseEntity.ok(verificationService.getVerificationById(verificationId));
//    }
//
//    @GetMapping("/provider/{providerId}")
//    @Operation(summary = "Get all verifications submitted by a provider")
//    public ResponseEntity<List<ProviderVerificationResponseDTO>> getVerificationsByProvider(
//            @PathVariable Long providerId) {
//        return ResponseEntity.ok(verificationService.getVerificationsByProviderId(providerId));
//    }
//
//    @GetMapping
//    @Operation(summary = "Get all verifications (Admin only)")
//    public ResponseEntity<List<ProviderVerificationResponseDTO>> getAllVerifications() {
//        return ResponseEntity.ok(verificationService.getAllVerifications());
//    }
}
