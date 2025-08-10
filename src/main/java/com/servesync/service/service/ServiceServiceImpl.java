package com.servesync.service.service;


import com.servesync.dto.service.ServiceDTO;
import com.servesync.dto.service.ServiceWithSubServiceDTO;
import com.servesync.dto.service.SubServiceDTO;
import com.servesync.entity.service.Service;
import com.servesync.entity.service.SubService;
import com.servesync.repository.service.ServiceRepository;
import com.servesync.repository.service.SubServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
	
	@Autowired
    private ServiceRepository serviceRepository;

    private SubServiceRepository subServiceRepository;

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public String createService(ServiceDTO service) {
    	
    	Service serviceen = new Service();
    	serviceen.setServiceName(service.getServiceName());
    	serviceen.setDescription(service.getDescription());
        serviceRepository.save(serviceen);
        		return "Service Saved";
    }

    @Override
    @Transactional
    public Service updateService(Long id, Service serviceDetails) {
        Optional<Service> optionalService = serviceRepository.findById(id);
        if (!optionalService.isPresent()) return null;

        Service service = optionalService.get();
        service.setServiceName(serviceDetails.getServiceName());
        service.setDescription(serviceDetails.getDescription());
        service.setIsActive(serviceDetails.getIsActive());

        return serviceRepository.save(service);
    }

    @Override
    @Transactional
    public boolean deleteService(Long id) {
        if (!serviceRepository.existsById(id)) return false;
        serviceRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public String addServiceWithSubService(ServiceWithSubServiceDTO dto) {
        Optional<Service> optionalService = serviceRepository.findByServiceName(dto.getServiceName());
        Service service;
        try {
        	if (optionalService.isPresent()) {
                service = optionalService.get();
            } else {
                service = new Service();
                service.setServiceName(dto.getServiceName());
                service.setIsActive(true);
                service = serviceRepository.save(service);
            }

            SubService subService = new SubService();
            subService.setSubServiceName(dto.getSubServiceName());
            subService.setDescription(dto.getDescription());
            subService.setIsActive(true);
            subService.setService(service);

            service.addSubService(subService);
            serviceRepository.save(service); // Save with cascading
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        
        
        return "Sub_Service with Service Added"; 
    }
    
    
    @Transactional
    @Override
    public Set<SubServiceDTO> getSubServicesByServiceId(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        return service.getSubServices().stream().map(this::convertToDTO).collect(Collectors.toSet());
    }

    private SubServiceDTO convertToDTO(SubService subService) {
        SubServiceDTO dto = new SubServiceDTO();
        dto.setId(subService.getId());
        dto.setSubServiceName(subService.getSubServiceName());
        dto.setDescription(subService.getDescription());
        dto.setBasePrice(subService.getBasePrice());
        dto.setIsActive(subService.getIsActive());
        dto.setServiceId(subService.getService().getId());
        dto.setServiceName(subService.getService().getServiceName());
        return dto;
    }
}
