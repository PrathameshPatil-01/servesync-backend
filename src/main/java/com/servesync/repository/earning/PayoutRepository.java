package com.servesync.repository.earning;

import com.servesync.entity.earning.Payout;
import com.servesync.enums.PayoutStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {
    Page<Payout> findByProviderId(Long providerId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payout p WHERE p.provider.id = :providerId AND p.status IN :statuses AND p.isDeleted = FALSE")
    BigDecimal sumPayoutsByProviderAndStatuses(@Param("providerId") Long providerId, @Param("statuses") List<PayoutStatusEnum> statuses);
}
