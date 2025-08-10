package com.servesync.dto.schedule;

import com.servesync.dto.base.BaseDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockedTimeSlotResponseDTO extends BaseDTO {
    private Long id;
    private Long providerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
}
