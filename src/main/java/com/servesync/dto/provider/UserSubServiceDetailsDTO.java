package com.servesync.dto.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSubServiceDetailsDTO {
    private Long offerId;
    private Long subServiceId;
    private String subServiceName;
    private String subServiceDescription;
    private Long serviceId;
    private String serviceName;
    private BigDecimal basePrice;
    private String currency;
    private Integer estimatedDurationMinutes;
    private Boolean isActive;

    // provider info
    private Long providerId;
    private String providerFullName;
    private String providerBusinessName;
    private Double providerAverageRating;
    private Integer providerJobsCompleted;
    private Integer providerServiceAreaRadiusKm;
}
