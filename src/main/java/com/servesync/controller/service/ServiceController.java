package com.servesync.controller.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servesync.dto.service.AddServiceDto;
import com.servesync.dto.service.ServiceDto;
import com.servesync.service.service.ServiceService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor

public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> addService(
        @ModelAttribute AddServiceDto
        
        serviceDto) {
        String response = serviceService.addService(serviceDto);
        return ResponseEntity.ok(response);
    }
}
