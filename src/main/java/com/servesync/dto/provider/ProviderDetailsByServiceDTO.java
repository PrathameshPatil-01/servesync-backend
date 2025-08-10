package com.servesync.dto.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDetailsByServiceDTO {
    private Long providerId;
    private String businessName;
    private String fullName;
    private Double averageRating;
    private Integer jobsCompleted;
    private List<ProviderServiceGetDTO> serviceOffers; // offers (filtered for the requested service when used)
}
