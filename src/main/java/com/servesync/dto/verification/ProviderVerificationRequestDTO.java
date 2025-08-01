package com.servesync.dto.verification;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderVerificationRequestDTO {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotNull(message = "Document hash is required")
    private byte[] documentHash;

    @NotBlank(message = "Front image URL is required")
    private String frontImageUrl;

    private String backImageUrl;
}
