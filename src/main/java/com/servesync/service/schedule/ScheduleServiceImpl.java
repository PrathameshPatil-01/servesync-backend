package com.servesync.service.schedule;

import com.servesync.dto.schedule.BlockedTimeSlotRequestDTO;
import com.servesync.dto.schedule.BlockedTimeSlotResponseDTO;
import com.servesync.dto.schedule.ScheduleEntryResponseDTO;
import com.servesync.dto.schedule.WorkingHoursRequestDTO;
import com.servesync.dto.schedule.WorkingHoursResponseDTO;
import com.servesync.entity.order.Order;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.schedule.BlockedTimeSlot;
import com.servesync.entity.schedule.WorkingHours;
import com.servesync.enums.DayOfWeekEnum;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.ScheduleEntryType;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.provider.ProviderRepository;
import com.servesync.repository.schedule.BlockedTimeSlotRepository;
import com.servesync.repository.schedule.WorkingHoursRepository;
import com.servesync.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.modelmapper.PropertyMap; // Import PropertyMap
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ProviderRepository providerRepository;
    private final OrderRepository orderRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final BlockedTimeSlotRepository blockedTimeSlotRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper
    private final SecurityUtils securityUtils;

    @Override
    public List<ScheduleEntryResponseDTO> getDailySchedule(Long providerId, LocalDate date) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Fetch confirmed/in-progress/completed orders for the day
        List<Order> orders = orderRepository.findProviderOrdersForDate(providerId, startOfDay, endOfDay);
        List<ScheduleEntryResponseDTO> orderEntries = orders.stream()
                .map(order -> ScheduleEntryResponseDTO.builder()
                        .id(order.getId())
                        .type(ScheduleEntryType.BOOKING)
                        .startTime(order.getScheduledStart())
                        .endTime(order.getScheduledEnd())
                        .title("Service with " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
                        .description(order.getProviderServiceOffer().getSubService().getSubServiceName())
                        .status(order.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        // Fetch blocked time slots for the day
        List<BlockedTimeSlot> blockedSlots = blockedTimeSlotRepository.findByProviderIdAndDateRange(providerId, startOfDay, endOfDay);
        List<ScheduleEntryResponseDTO> blockedEntries = blockedSlots.stream()
                .map(slot -> ScheduleEntryResponseDTO.builder()
                        .id(slot.getId())
                        .type(ScheduleEntryType.BLOCKED)
                        .startTime(slot.getStartTime())
                        .endTime(slot.getEndTime())
                        .title("Blocked Time")
                        .description(slot.getReason())
                        .status("BLOCKED")
                        .build())
                .collect(Collectors.toList());

        // Combine and sort entries by start time
        return Stream.concat(orderEntries.stream(), blockedEntries.stream())
                .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkingHoursResponseDTO> getWorkingHours(Long providerId) {
        providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        List<WorkingHours> workingHours = workingHoursRepository.findByProviderId(providerId);
        return workingHours.stream()
                .map(wh -> modelMapper.map(wh, WorkingHoursResponseDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkingHoursResponseDTO> updateWorkingHours(Long providerId, List<WorkingHoursRequestDTO> dtos) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        List<WorkingHours> updatedHoursList = new ArrayList<>();

        for (WorkingHoursRequestDTO dto : dtos) {
            DayOfWeekEnum dayOfWeek = DayOfWeekEnum.valueOf(dto.getDayOfWeek().toUpperCase());
            WorkingHours workingHours = workingHoursRepository.findByProviderIdAndDayOfWeek(providerId, dayOfWeek)
                    .orElseGet(() -> {
                        WorkingHours newHours = new WorkingHours();
                        newHours.setProvider(provider);
                        newHours.setDayOfWeek(dayOfWeek);
                        return newHours;
                    });

            // Manually map fields that require parsing or specific logic
            workingHours.setStartTime(LocalTime.parse(dto.getStartTime()));
            workingHours.setEndTime(LocalTime.parse(dto.getEndTime()));
            workingHours.setIsWorking(dto.getIsWorking());

            updatedHoursList.add(workingHoursRepository.save(workingHours));
        }
        log.info("Working hours updated for provider ID: {}", providerId);
        return updatedHoursList.stream()
                .map(wh -> modelMapper.map(wh, WorkingHoursResponseDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public BlockedTimeSlotResponseDTO addBlockedTimeSlot(Long providerId, BlockedTimeSlotRequestDTO dto) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new ApiException("Blocked slot start time must be before end time.");
        }

        BlockedTimeSlot blockedSlot = modelMapper.map(dto, BlockedTimeSlot.class); // Use ModelMapper
        blockedSlot.setProvider(provider);

        BlockedTimeSlot savedSlot = blockedTimeSlotRepository.save(blockedSlot);
        log.info("Blocked time slot added for provider ID: {}", providerId);
        return modelMapper.map(savedSlot, BlockedTimeSlotResponseDTO.class); // Use ModelMapper
    }

    @Override
    public void deleteBlockedTimeSlot(Long slotId) {
        BlockedTimeSlot slot = blockedTimeSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Blocked time slot not found with ID: " + slotId));
        slot.setIsDeleted(true); // Soft delete
        blockedTimeSlotRepository.save(slot);
        log.info("Blocked time slot ID {} soft-deleted.", slotId);
    }

    @Override
    public boolean isBlockedSlotOwnedByCurrentUser(Long slotId) {
        Long currentProviderId = securityUtils.getCurrentProviderId();
        return blockedTimeSlotRepository.existsByIdAndProviderId(slotId, currentProviderId);
    }
}
