package com.servesync.dto.provider;

import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.service.SubServiceDTO;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceOfferDTO extends BaseDTO {
    private Long id;
    private Long providerId;
    private SubServiceDTO subService; // Changed to DTO
    private BigDecimal price;
    private String currency;
    private Integer estimatedDurationMinutes;
    private Boolean isActive;
}
