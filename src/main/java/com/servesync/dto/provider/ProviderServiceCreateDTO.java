package com.servesync.dto.provider;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceCreateDTO {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "SubService ID is required")
    private Long subServiceId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Currency is required")
    @Size(max = 10, message = "Currency code max length is 10")
    private String currency;

    @NotNull(message = "Estimated duration is required")
    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;

    private Boolean isActive = true;
}
