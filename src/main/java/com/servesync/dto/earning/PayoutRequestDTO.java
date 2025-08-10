package com.servesync.dto.earning;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayoutRequestDTO {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    // Optional: bank account details if not already linked to provider profile
    private String bankAccountNumber;
    private String ifscCode;
}
