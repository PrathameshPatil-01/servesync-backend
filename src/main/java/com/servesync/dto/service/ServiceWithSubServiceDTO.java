package com.servesync.dto.service;

import lombok.Data;

@Data
public class ServiceWithSubServiceDTO {
    private String serviceName;
    private String subServiceName;
    private String description; 
}
