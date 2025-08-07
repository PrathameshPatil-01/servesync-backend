package com.servesync.entity.earning;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.order.Order;
import com.servesync.entity.provider.Provider;
import com.servesync.enums.EarningTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "earnings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Earning extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true) // Earning linked to a completed order
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "earning_type", nullable = false)
    private EarningTypeEnum earningType; // e.g., SERVICE_COMPLETION, BONUS, REFUND

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "INR";

    @Column(name = "earning_date", nullable = false)
    private LocalDateTime earningDate;

    @Column(name = "description")
    private String description;
}
