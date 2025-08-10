package com.servesync.entity.order;

import com.servesync.entity.address.Address;
import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.provider.ProviderServiceOffer;
import com.servesync.entity.user.User;
import com.servesync.enums.OrderStatusEnum; // Renamed from BookingStatusEnum

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_dates", columnList = "scheduled_start, scheduled_end"),
        @Index(name = "idx_order_provider_status", columnList = "provider_service_offer_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer; // Renamed from user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_service_offer_id", nullable = false)
    private ProviderServiceOffer providerServiceOffer; // Renamed from providerService

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_address_id", nullable = false)
    private Address serviceAddress;

    @Column(name = "scheduled_start", nullable = false)
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end", nullable = false)
    private LocalDateTime scheduledEnd;

    @Column(name = "actual_start")
    private LocalDateTime actualStart;

    @Column(name = "actual_end")
    private LocalDateTime actualEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatusEnum status; // Renamed from BookingStatusEnum

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "travel_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal travelFee = BigDecimal.ZERO;

    @Column(name = "cancellation_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal cancellationFee = BigDecimal.ZERO;

    @Column(name = "special_requests")
    private String specialRequests;
}
