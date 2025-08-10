package com.servesync.dto.verification;

import com.servesync.enums.VerificationStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProviderVerificationResponseDTO {

    private Long id;
    private Long providerId;
    private String documentType;
    private String documentNumber;
    private String frontImageUrl;
    private String backImageUrl;
    private VerificationStatusEnum status;
    private String verifiedBy;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;
}
