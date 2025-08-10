package com.servesync.controller.schedule;

import com.servesync.dto.schedule.BlockedTimeSlotRequestDTO;
import com.servesync.dto.schedule.BlockedTimeSlotResponseDTO;
import com.servesync.dto.schedule.ScheduleEntryResponseDTO;
import com.servesync.dto.schedule.WorkingHoursRequestDTO;
import com.servesync.dto.schedule.WorkingHoursResponseDTO;
import com.servesync.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Validated
@Tag(name = "Provider Schedule Management", description = "APIs for managing provider's working hours and blocked time slots")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/provider/{providerId}/daily")
    @Operation(summary = "Get daily schedule for a provider (bookings and blocked slots)")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<List<ScheduleEntryResponseDTO>> getDailySchedule(
            @PathVariable @Min(1) Long providerId,
            @RequestParam @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format") String date) {
        return ResponseEntity.ok(scheduleService.getDailySchedule(providerId, LocalDate.parse(date)));
    }

    @GetMapping("/provider/{providerId}/working-hours")
    @Operation(summary = "Get working hours for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<List<WorkingHoursResponseDTO>> getWorkingHours(@PathVariable @Min(1) Long providerId) {
        return ResponseEntity.ok(scheduleService.getWorkingHours(providerId));
    }

    @PutMapping("/provider/{providerId}/working-hours")
    @Operation(summary = "Update working hours for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<List<WorkingHoursResponseDTO>> updateWorkingHours(
            @PathVariable @Min(1) Long providerId,
            @Valid @RequestBody List<WorkingHoursRequestDTO> dtos) {
        return ResponseEntity.ok(scheduleService.updateWorkingHours(providerId, dtos));
    }

    @PostMapping("/provider/{providerId}/blocked-slots")
    @Operation(summary = "Add a blocked time slot for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<BlockedTimeSlotResponseDTO> addBlockedTimeSlot(
            @PathVariable @Min(1) Long providerId,
            @Valid @RequestBody BlockedTimeSlotRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.addBlockedTimeSlot(providerId, dto));
    }

    @DeleteMapping("/blocked-slots/{slotId}")
    @Operation(summary = "Delete a blocked time slot")
    @PreAuthorize("hasRole('PROVIDER') and @scheduleService.isBlockedSlotOwnedByCurrentUser(#slotId) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBlockedTimeSlot(@PathVariable @Min(1) Long slotId) {
        scheduleService.deleteBlockedTimeSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}
