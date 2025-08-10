package com.servesync.dto.order;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Provider service offer ID is required")
    private Long providerServiceOfferId;

    @NotNull(message = "Service address ID is required")
    private Long serviceAddressId;

    @NotNull(message = "Scheduled start is required")
    @FutureOrPresent(message = "Scheduled start time must be in the present or future")
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

    private String specialRequests;
}
