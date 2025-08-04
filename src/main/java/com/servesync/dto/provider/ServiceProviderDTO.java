package com.servesync.dto.provider;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {
    private Long id;
    private Long userId;

    private String fullName;
    private String aadharNumber;
    private String panNumber;
    private String skills;
    private String description;
    private Integer yearsOfExperience;

    private String profileImage; // Base64-encoded string for the image

    private Set<ProviderServiceDTO> providerServices;
}
