package com.servesync.dto.provider;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProviderDashboardStatsDTO {
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private Long upcomingAppointments;
    private BigDecimal totalEarnings;
    private BigDecimal todayEarnings;
    private BigDecimal thisWeekEarnings;
    private BigDecimal thisMonthEarnings;
}
