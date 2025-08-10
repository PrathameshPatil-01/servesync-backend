package com.servesync.dto.order;

import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.dto.provider.ProviderServiceOfferDTO;
import com.servesync.dto.user.UserSummaryDTO;
import com.servesync.enums.OrderStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO extends BaseDTO {
    private Long id;
    private UserSummaryDTO customer; // Renamed from userId
    private ProviderServiceOfferDTO providerServiceOffer; // Renamed from providerServiceId
    private AddressResponseDTO serviceAddress; // Renamed from serviceAddressId
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private OrderStatusEnum status; // Renamed from BookingStatusEnum
    private BigDecimal totalPrice;
    private BigDecimal travelFee;
    private BigDecimal cancellationFee;
    private String specialRequests;
}
