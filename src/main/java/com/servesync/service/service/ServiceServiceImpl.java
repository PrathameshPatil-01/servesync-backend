package com.servesync.service.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.servesync.dto.service.AddServiceDto;
import com.servesync.dto.service.ServiceDto;
import com.servesync.entity.service.Service;
import com.servesync.repository.service.ServiceRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@org.springframework.stereotype.Service
@Transactional
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream().map(service -> {
            ServiceDto dto = new ServiceDto();
            dto.setServiceName(service.getServiceName());
            if (service.getImageData() != null) {
                dto.setImageBase64(Base64.getEncoder().encodeToString(service.getImageData()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

	@Override
	public String addService(AddServiceDto serviceDto) {
		Service service = new Service();
	    service.setServiceName(serviceDto.getServiceName());
	    service.setDescription(serviceDto.getDescription());
	    service.setIsActive(serviceDto.getIsActive() != null ? serviceDto.getIsActive() : true);

	    try {
	        if (serviceDto.getImageFile() != null && !serviceDto.getImageFile().isEmpty()) {
	            service.setImageData(serviceDto.getImageFile().getBytes());
	        }
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to read image file", e);
	    }

	    serviceRepository.save(service);
	    return "Service added successfully"; // ✅ RETURN STATEMENT
	}

}

