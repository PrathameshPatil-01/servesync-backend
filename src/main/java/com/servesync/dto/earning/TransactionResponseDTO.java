package com.servesync.dto.earning;

import com.servesync.dto.base.BaseDTO;
import com.servesync.enums.TransactionTypeEnum;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDTO extends BaseDTO {
    private Long id;
    private Long providerId;
    private Long relatedEntityId; // Order ID, Payout ID etc.
    private TransactionTypeEnum type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
}
