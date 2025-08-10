package com.servesync.dto.payment;

import com.servesync.enums.PaymentMethodEnum;
import com.servesync.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;

    private Long bookingId;

    private BigDecimal amount;

    private String currency;

    private PaymentMethodEnum method;

    private PaymentStatusEnum status;

    private String gatewayTransactionId;

    private String gatewayResponse;

    private LocalDateTime paidAt;
}
