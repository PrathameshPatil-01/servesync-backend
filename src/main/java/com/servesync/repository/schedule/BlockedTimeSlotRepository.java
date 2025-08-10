package com.servesync.repository.schedule;

import com.servesync.entity.schedule.BlockedTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlockedTimeSlotRepository extends JpaRepository<BlockedTimeSlot, Long> {
    @Query("SELECT b FROM BlockedTimeSlot b WHERE b.provider.id = :providerId " +
            "AND b.startTime BETWEEN :startOfDay AND :endOfDay AND b.isDeleted = FALSE")
    List<BlockedTimeSlot> findByProviderIdAndDateRange(@Param("providerId") Long providerId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM BlockedTimeSlot b WHERE b.id = :slotId AND b.provider.id = :providerId AND b.isDeleted = FALSE")
    boolean existsByIdAndProviderId(@Param("slotId") Long slotId, @Param("providerId") Long providerId);
}
