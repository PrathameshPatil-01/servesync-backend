package com.servesync.dto.provider;


import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.servesync.dto.user.UserSummaryDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderResponseDTO {
    private Long id; // if not using base, otherwise inherited

    private UserSummaryDTO user;

    private String businessName;
    private String fullName;

    private String aadharNumber;
    private String gstNumber;
    private String panNumber;
    private String bio;

    private Integer yearsOfExperience;
    private boolean isVerified;
    private LocalDateTime verifiedAt;

    private String availableDays;
    private LocalTime availableTimeStart;
    private LocalTime availableTimeEnd;

    private Integer jobsCompleted;
    private Integer reviewCount;
    private Double averageRating;
    private Integer serviceAreaRadiusKm;

    private Set<String> documents;

    private Set<ProviderServiceDTO> providerServices;
}

