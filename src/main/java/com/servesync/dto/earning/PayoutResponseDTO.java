package com.servesync.dto.earning;

import com.servesync.dto.base.BaseDTO;
import com.servesync.enums.PayoutStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayoutResponseDTO extends BaseDTO {
    private Long id;
    private Long providerId;
    private BigDecimal amount;
    private PayoutStatusEnum status;
    private String transactionId; // Reference ID from payment gateway/bank
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}
