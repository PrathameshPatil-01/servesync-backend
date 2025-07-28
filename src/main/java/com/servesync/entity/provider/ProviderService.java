package com.servesync.entity.provider;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.service.SubService;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_services",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"provider_id", "sub_service_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderService extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_service_id", nullable = false)
    private SubService subService;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String currency = "INR";

    @Column(name = "estimated_duration", nullable = false)
    private Integer estimatedDuration; // in minutes

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

