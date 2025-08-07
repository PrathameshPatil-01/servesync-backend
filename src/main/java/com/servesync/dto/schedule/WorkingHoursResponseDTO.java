package com.servesync.dto.schedule;

import com.servesync.enums.DayOfWeekEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class WorkingHoursResponseDTO {
    private Long id;
    private Long providerId;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isWorking;
}
