package com.servesync.dto.provider;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderUpdateDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Size(min = 12, max = 12, message = "Aadhar number must be exactly 12 digits")
    private String aadharNumber;

    @Size(min = 10, max = 10, message = "PAN number must be exactly 10 characters")
    private String panNumber;

    @Size(max = 200, message = "Skills can't exceed 200 characters")
    private String skills;

    @Size(max = 500, message = "Description can't exceed 500 characters")
    private String description;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be 0 or more")
    @Max(value = 100, message = "Years of experience must be reasonable (max 100)")
    private Integer yearsOfExperience;

    private String profileImage; // Base64-encoded image string
}
