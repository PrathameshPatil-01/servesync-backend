package com.servesync.repository.service;

import com.servesync.entity.service.SubService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {

    List<SubService> findByServiceId(Long serviceId);

}
