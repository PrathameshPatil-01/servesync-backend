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
public class ProviderRequestDTO {

//    @NotNull(message = "User ID is required")
//    private Long userId; // Link to existing user

//    @NotBlank(message = "Business name is required")
//    @Size(max = 255, message = "Business name max length is 255 characters")
    private String businessName;

//    @NotBlank(message = "Full name is required")
//    @Size(max = 100, message = "Full name max length is 100 characters")
    private String fullName;

//    @NotBlank(message = "Aadhar number is required")
//    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhar number must be 12 digits")
    private String aadharNumber;

//    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GST number format")
    private String gstNumber;

//    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN number format")
    private String panNumber;

    @Size(max = 1000, message = "Bio max length is 1000 characters")
    private String bio;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    @Size(max = 50, message = "Available days max length is 50 characters")
    private String availableDays; // e.g., "Mon,Tue,Wed"

    private LocalTime availableTimeStart;
    private LocalTime availableTimeEnd;

    @NotNull(message = "Service area radius is required")
    @Min(value = 0, message = "Service area radius cannot be negative")
    private Integer serviceAreaRadiusKm;

    private Set<String> documents; // URLs to documents
}
