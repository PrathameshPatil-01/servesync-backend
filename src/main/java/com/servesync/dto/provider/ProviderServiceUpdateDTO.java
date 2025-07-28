package com.servesync.dto.provider;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceUpdateDTO {

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private Double price;

    @Size(max = 10, message = "Currency code max length is 10")
    private String currency;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDuration;

    private Boolean isActive;
}
