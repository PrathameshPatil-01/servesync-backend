package com.servesync.service.schedule;

import com.servesync.dto.schedule.BlockedTimeSlotRequestDTO;
import com.servesync.dto.schedule.BlockedTimeSlotResponseDTO;
import com.servesync.dto.schedule.ScheduleEntryResponseDTO;
import com.servesync.dto.schedule.WorkingHoursRequestDTO;
import com.servesync.dto.schedule.WorkingHoursResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    List<ScheduleEntryResponseDTO> getDailySchedule(Long providerId, LocalDate date);
    List<WorkingHoursResponseDTO> getWorkingHours(Long providerId);
    List<WorkingHoursResponseDTO> updateWorkingHours(Long providerId, List<WorkingHoursRequestDTO> dtos);
    BlockedTimeSlotResponseDTO addBlockedTimeSlot(Long providerId, BlockedTimeSlotRequestDTO dto);
    void deleteBlockedTimeSlot(Long slotId);

    // Security helper method
    boolean isBlockedSlotOwnedByCurrentUser(Long slotId);
}
