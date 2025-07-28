package com.servesync.dto.provider;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderUpdateDTO {

    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    private String businessName;

    @Size(max = 50, message = "Business registration number can't exceed 50 characters")
    private String businessRegNumber;

    @Size(max = 50, message = "Business tax ID can't exceed 50 characters")
    private String businessTaxId;

    @Size(max = 500, message = "Description can't exceed 500 characters")
    private String description;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be 0 or more")
    @Max(value = 100, message = "Years of experience must be reasonable (max 100)")
    private Integer yearsOfExperience;
}
