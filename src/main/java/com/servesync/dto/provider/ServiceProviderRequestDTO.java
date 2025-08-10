package com.servesync.dto.provider;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderRequestDTO {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Aadhar number is required")
    private String aadharNumber;

    private String gstNumber;
    private String panNumber;

    @Size(max = 1000)
    private String bio;

    @NotNull
    @Min(0)
    private Integer yearsOfExperience;

//    private String availableDays; // e.g., Mon,Tue
    private LocalTime availableTimeStart;
    private LocalTime availableTimeEnd;

    @Min(0)
    private Integer serviceAreaRadiusKm;

    private Set<String> documents;

    private Set<Long> providerServiceIds; // IDs of linked services
}

