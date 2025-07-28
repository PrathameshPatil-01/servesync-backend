package com.servesync.dto.provider;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {
    private Long id;
    private Long userId;
    private String businessName;
    private String businessRegNumber;
    private String businessTaxId;
    private String description;
    private Integer yearsOfExperience;
    private Set<ProviderServiceDTO> providerServices;
}
