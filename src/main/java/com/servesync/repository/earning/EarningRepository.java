package com.servesync.repository.earning;

import com.servesync.entity.earning.Earning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EarningRepository extends JpaRepository<Earning, Long> {
    Page<Earning> findByProviderIdAndEarningDateBetween(Long providerId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Earning> findByProviderIdAndEarningDateBetween(Long providerId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Earning e WHERE e.provider.id = :providerId AND e.earningDate >= :startDate AND e.earningDate <= :endDate AND e.isDeleted = FALSE")
    BigDecimal sumEarningsByProviderAndDateRange(@Param("providerId") Long providerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Earning e WHERE e.provider.id = :providerId AND e.isDeleted = FALSE")
    BigDecimal sumTotalEarningsByProvider(@Param("providerId") Long providerId);
}
