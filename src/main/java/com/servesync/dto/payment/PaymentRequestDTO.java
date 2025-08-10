package com.servesync.dto.payment;

import com.servesync.enums.PaymentMethodEnum;
import com.servesync.enums.PaymentStatusEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "Booking ID must not be null")
    private Long orderId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid decimal with up to 2 decimal places")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter uppercase code")
    private String currency = "INR";

    @NotNull(message = "Payment method is required")
    private PaymentMethodEnum method;

    @NotNull(message = "Payment status is required")
    private PaymentStatusEnum status;

    private String gatewayTransactionId;

    private String gatewayResponse;

    @PastOrPresent(message = "Paid date can't be in the future")
    private LocalDateTime paidAt;

}
