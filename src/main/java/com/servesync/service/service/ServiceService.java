package com.servesync.service.service;

import java.security.Provider.Service;
import java.util.List;
import java.util.Optional;

import com.servesync.dto.services.AddServiceDto;
import com.servesync.dto.services.ServiceDto;

public interface ServiceService {

	
	List<ServiceDto> getAllServices();
	String addService(AddServiceDto serviceDto);
}
