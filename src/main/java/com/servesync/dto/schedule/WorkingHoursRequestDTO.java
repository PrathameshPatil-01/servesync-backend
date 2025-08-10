package com.servesync.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WorkingHoursRequestDTO {
    @NotBlank(message = "Day of week is required")
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$", message = "Invalid day of week")
    private String dayOfWeek;

    @NotBlank(message = "Start time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Start time must be in HH:MM format")
    private String startTime; // HH:MM

    @NotBlank(message = "End time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "End time must be in HH:MM format")
    private String endTime; // HH:MM

    private Boolean isWorking = true;
}
