package com.servesync.dto.earning;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EarningSummaryDTO {
    private BigDecimal todayEarnings;
    private BigDecimal thisWeekEarnings;
    private BigDecimal thisMonthEarnings;
    private BigDecimal totalEarnings;
    private BigDecimal availableForPayout;
    private BigDecimal pendingPayouts;
}
