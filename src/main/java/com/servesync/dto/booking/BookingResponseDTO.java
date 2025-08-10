package com.servesync.dto.booking;

import com.servesync.enums.BookingStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private Long id;
    private Long userId;
    private Long providerServiceId;
    private Long serviceAddressId;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private BookingStatusEnum status;
    private BigDecimal totalPrice;
    private BigDecimal travelFee;
    private BigDecimal cancellationFee;
    private String specialRequests;
}
