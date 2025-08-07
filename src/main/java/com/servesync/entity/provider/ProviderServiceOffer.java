package com.servesync.entity.provider;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.service.SubService;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "provider_service_offers", // Renamed from provider_services
       uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_id", "sub_service_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceOffer extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider; // Renamed from ServiceProvider

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_service_id", nullable = false)
    private SubService subService;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency = "INR";

    @Column(name = "estimated_duration_minutes", nullable = false) // Renamed
    private Integer estimatedDurationMinutes; // in minutes

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
