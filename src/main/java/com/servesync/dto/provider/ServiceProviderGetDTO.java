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
public class ServiceProviderGetDTO {
    private Long providerId;
    private Long userId;
    private String businessName;
    private String fullName;
    private Double averageRating;
    private Integer jobsCompleted;
    private Integer serviceAreaRadiusKm;
    private Boolean isVerified;
    private List<ProviderServiceGetDTO> services;
}
