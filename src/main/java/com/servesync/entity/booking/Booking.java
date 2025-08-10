package com.servesync.entity.booking;

import com.servesync.entity.address.Address;
import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.provider.ProviderService;
import com.servesync.entity.user.User;
import com.servesync.enums.BookingStatusEnum;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
		@Index(name = "idx_booking_dates", columnList = "scheduled_start, scheduled_end") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntityWithId {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provider_service_id", nullable = false)
	private ProviderService providerService;

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
	private BookingStatusEnum status;

	@Column(name = "total_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalPrice;

	@Column(name = "travel_fee", nullable = false, precision = 10, scale = 2)
	private BigDecimal travelFee = BigDecimal.ZERO;

	@Column(name = "cancellation_fee", nullable = false, precision = 10, scale = 2)
	private BigDecimal cancellationFee = BigDecimal.ZERO;

	@Column(name = "special_requests")
	private String specialRequests;
}