package com.servesync.entity.service;

import java.math.BigDecimal;

import com.servesync.entity.base.BaseEntityWithId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_services",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"service_id", "sub_service_name"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubService extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "sub_service_name", nullable = false)
    private String subServiceName;

    private String description;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}