package com.servesync.repository.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.service.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {

	Optional<Service> findByServiceName(String serviceName);
	List<Service> findByIsActiveTrue();
	
}
