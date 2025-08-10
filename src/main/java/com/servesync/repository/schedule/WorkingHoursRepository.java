package com.servesync.repository.schedule;

import com.servesync.entity.schedule.WorkingHours;
import com.servesync.enums.DayOfWeekEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findByProviderId(Long providerId);
    Optional<WorkingHours> findByProviderIdAndDayOfWeek(Long providerId, DayOfWeekEnum dayOfWeek);
}
