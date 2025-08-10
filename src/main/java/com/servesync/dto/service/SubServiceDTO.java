package com.servesync.dto.service;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SubServiceDTO {
    private Long id;
    private String subServiceName;
    private String description;
    private BigDecimal basePrice;
    private Boolean isActive;
    private Long serviceId; // Parent service ID
    private String serviceName; // Parent service name
}
