package com.servesync.dto.order;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for creating or updating an Order.
 * Fields here are primarily IDs and basic values — entities are resolved in the service layer.
 */
@Data
public class OrderRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Provider service offer ID is required")
    private Long providerServiceOfferId;

    @NotNull(message = "Service address ID is required")
    private Long serviceAddressId;

    @NotNull(message = "Scheduled start is required")
    @FutureOrPresent(message = "Scheduled start time must be now or in the future")
    private LocalDateTime scheduledStart;

    @NotNull(message = "Scheduled end is required")
    @Future(message = "Scheduled end time must be in the future")
    private LocalDateTime scheduledEnd;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be positive")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0.0", message = "Travel fee cannot be negative")
    private BigDecimal travelFee = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Cancellation fee cannot be negative")
    private BigDecimal cancellationFee = BigDecimal.ZERO;

    @Size(max = 500, message = "Special requests cannot exceed 500 characters")
    private String specialRequests;
}
