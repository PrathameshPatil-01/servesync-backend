package com.servesync.dto.schedule;

import com.servesync.enums.ScheduleEntryType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleEntryResponseDTO {
    private Long id; // Order ID or Blocked Slot ID
    private ScheduleEntryType type; // BOOKING or BLOCKED
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title; // e.g., "Service with John Doe" or "Blocked for personal"
    private String description; // e.g., service name or reason for blocking
    private String status; // OrderStatusEnum or "BLOCKED"
}
