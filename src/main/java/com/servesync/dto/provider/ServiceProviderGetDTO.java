

package com.servesync.dto.provider;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderGetDTO {

    private Long id;

    private Long userId;  // user ID owning the provider

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

    private Set<ProviderServiceGetDTO> providerServices;  // Updated to ProviderServiceGetDTO

}
