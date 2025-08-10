package com.servesync.service.service;

import com.servesync.entity.service.Service;
import com.servesync.entity.service.SubService;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.service.ServiceRepository;
import com.servesync.repository.service.SubServiceRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class SubServiceServiceImpl implements SubServiceService {

    private final SubServiceRepository subServiceRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public SubService createSubService(Long serviceId, SubService subService) {
        Service parentService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));
        subService.setService(parentService);
        return subServiceRepository.save(subService);
    }

    @Override
    public SubService updateSubService(Long id, SubService updatedSubService) {
        SubService existing = subServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubService not found with ID: " + id));

        existing.setSubServiceName(updatedSubService.getSubServiceName());
        existing.setDescription(updatedSubService.getDescription());
        existing.setBasePrice(updatedSubService.getBasePrice());
        existing.setIsActive(updatedSubService.getIsActive());

        return subServiceRepository.save(existing);
    }

    @Override
    public void deleteSubService(Long id) {
        SubService subService = subServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubService not found with ID: " + id));
        subServiceRepository.delete(subService);
    }

    @Override
    public SubService getSubServiceById(Long id) {
        return subServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubService not found with ID: " + id));
    }

    @Override
    public List<SubService> getAllSubServices() {
        return subServiceRepository.findAll();
    }

    @Override
    public List<SubService> getSubServicesByServiceId(Long serviceId) {
        return subServiceRepository.findByServiceId(serviceId);
    }
}
