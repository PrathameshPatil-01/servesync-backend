package com.servesync.service.service;

import com.servesync.entity.service.SubService;

import java.util.List;

public interface SubServiceService {

    SubService createSubService(Long serviceId, SubService subService);

    SubService updateSubService(Long id, SubService subService);

    void deleteSubService(Long id);

    SubService getSubServiceById(Long id);

    List<SubService> getAllSubServices();

    List<SubService> getSubServicesByServiceId(Long serviceId);
}
