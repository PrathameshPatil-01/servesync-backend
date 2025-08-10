package com.servesync.entity.order;

import com.servesync.entity.address.Address;
import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.provider.ProviderServiceOffer;
import com.servesync.entity.user.User;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.PaymentStatusEnum;
import com.servesync.enums.PaymentMethodEnum;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_order_dates", columnList = "scheduled_start, scheduled_end"),
        @Index(name = "idx_order_provider_status", columnList = "provider_id, status"),
        @Index(name = "idx_order_customer", columnList = "customer_id"),
        @Index(name = "idx_order_offer", columnList = "provider_service_offer_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntityWithId {

    /** The customer who booked the service */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    /** The provider assigned to fulfill the order */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    /** The provider’s specific service offer chosen by the customer */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_service_offer_id", nullable = false)
    private ProviderServiceOffer providerServiceOffer;

    /** Service location */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_address_id", nullable = false)
    private Address serviceAddress;

    /** Scheduled service window */
    @Column(name = "scheduled_start", nullable = false)
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end", nullable = false)
    private LocalDateTime scheduledEnd;

    /** Actual service timings */
    @Column(name = "actual_start")
    private LocalDateTime actualStart;

    @Column(name = "actual_end")
    private LocalDateTime actualEnd;

    /** Current order status */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatusEnum status;

    /** Financials */
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "travel_fee", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal travelFee = BigDecimal.ZERO;

    @Column(name = "cancellation_fee", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal cancellationFee = BigDecimal.ZERO;

    /** Special customer instructions */
    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    /** Payment details */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethodEnum paymentMethod;

    /** Cancellation tracking */
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;
}
