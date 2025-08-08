package com.servesync.service.service;

import com.servesync.dto.service.ServiceDTO;
import com.servesync.dto.service.ServiceWithSubServiceDTO;
import com.servesync.dto.service.SubServiceDTO;
import com.servesync.entity.service.Service;

import java.util.List;
import java.util.Set;

public interface ServiceService {
    List<Service> getAllServices();
    Service getServiceById(Long id);
    String createService(ServiceDTO service);
    Service updateService(Long id, Service service);
    boolean deleteService(Long id);
    String addServiceWithSubService(ServiceWithSubServiceDTO dto);
    Set<SubServiceDTO> getSubServicesByServiceId(Long serviceId);
}