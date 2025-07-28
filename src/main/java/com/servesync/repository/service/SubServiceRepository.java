package com.servesync.repository.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.service.SubService;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {

	 Optional<SubService> findById(Long id);
	 List<SubService> findAll();

}
