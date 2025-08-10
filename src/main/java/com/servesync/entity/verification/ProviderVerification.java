package com.servesync.entity.verification;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.user.User;
import com.servesync.enums.VerificationStatusEnum;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderVerification extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "document_hash", nullable = false, columnDefinition = "BINARY(32)")
    private byte[] documentHash;

    @Column(name = "front_image_url", nullable = false)
    private String frontImageUrl;

    @Column(name = "back_image_url")
    private String backImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VerificationStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
}

