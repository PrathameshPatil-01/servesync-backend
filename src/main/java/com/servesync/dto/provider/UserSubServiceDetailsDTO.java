package com.servesync.dto.provider;

import lombok.Data;

@Data
public class UserSubServiceDetailsDTO {
	 private Long subServiceId; 
    private String serviceName;
    private String subServiceName;
    private String subServiceDescription;
    private Double price;
    private String currency;
    private Integer estimatedDuration;
}
