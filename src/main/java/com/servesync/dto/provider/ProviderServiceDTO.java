package com.servesync.dto.provider;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceDTO {
    private Long id;
    private Long providerId;
    private Long subServiceId;
    private Double price;
    private String currency;
    private Integer estimatedDuration;  // minutes
    private Boolean isActive;
}
