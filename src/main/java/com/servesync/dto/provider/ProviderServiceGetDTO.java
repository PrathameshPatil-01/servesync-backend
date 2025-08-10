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
public class ProviderServiceGetDTO {
    private Long offerId;               // provider service offer id
    private Long subServiceId;
    private String subServiceName;
    private Long serviceId;
    private String serviceName;
    private String currency;
    private Integer estimatedDurationMinutes;
    private Boolean isActive;
    private BigDecimal basePrice;           // from SubService.basePrice
    private String subServiceDescription;
}
