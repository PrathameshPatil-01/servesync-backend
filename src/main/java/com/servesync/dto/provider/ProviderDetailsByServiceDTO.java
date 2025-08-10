package com.servesync.dto.provider;

import lombok.Data;

@Data
public class ProviderDetailsByServiceDTO {
    private Long providerId;
    private String fullName;
    private String businessName;
    private String serviceName; // ✅ This is what you wanted
    private Integer estimatedDuration;
}
