package com.servesync.service.service;

import java.security.Provider.Service;
import java.util.List;
import java.util.Optional;

import com.servesync.dto.service.AddServiceDto;
import com.servesync.dto.service.ServiceDto;



public interface ServiceService {

	
	List<ServiceDto> getAllServices();
	String addService(AddServiceDto serviceDto);
}
