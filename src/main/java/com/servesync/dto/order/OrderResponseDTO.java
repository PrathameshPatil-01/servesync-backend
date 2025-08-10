package com.servesync.dto.order;

import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.provider.ProviderServiceOfferDTO;
import com.servesync.dto.user.UserSummaryDTO;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.PaymentMethodEnum;
import com.servesync.enums.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for sending detailed Order information to clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO extends BaseDTO {

    private Long id;

    /** Customer details */
    private UserSummaryDTO customer;

    /** Provider details */
    private UserSummaryDTO provider;

    /** Service offer details */
    private ProviderServiceOfferDTO providerServiceOffer;

    /** Address details */
    private AddressResponseDTO serviceAddress;

    /** Scheduled times */
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    /** Actual times */
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;

    /** Status */
    private OrderStatusEnum status;

    /** Financials */
    private BigDecimal totalPrice;
    private BigDecimal travelFee;
    private BigDecimal cancellationFee;

    /** Special requests */
    private String specialRequests;

    /** Payment info */
    private PaymentStatusEnum paymentStatus;
    private PaymentMethodEnum paymentMethod;

    /** Cancellation tracking */
    private LocalDateTime cancelledAt;
}
