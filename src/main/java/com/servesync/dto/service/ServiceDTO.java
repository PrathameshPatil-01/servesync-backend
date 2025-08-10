package com.servesync.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Data

public class ServiceDTO {
	private String serviceName;
	private String description;
}
