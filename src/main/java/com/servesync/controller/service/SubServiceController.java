package com.servesync.controller.service;

import com.servesync.dto.service.SubServiceDTO;
import com.servesync.entity.service.SubService;
import com.servesync.service.service.SubServiceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subservices")
@RequiredArgsConstructor
public class SubServiceController {

    private final SubServiceService subServiceService;

    // Convert entity to DTO (you can use ModelMapper if you want)
    private SubServiceDTO convertToDTO(SubService subService) {
        SubServiceDTO dto = new SubServiceDTO();
        dto.setId(subService.getId());
        dto.setSubServiceName(subService.getSubServiceName());
        dto.setDescription(subService.getDescription());
        dto.setBasePrice(subService.getBasePrice());
        dto.setIsActive(subService.getIsActive());
        if (subService.getService() != null) {
            dto.setServiceId(subService.getService().getId());
            dto.setServiceName(subService.getService().getServiceName());
        }
        return dto;
    }

    // Convert DTO to entity (for create/update)
    private SubService convertToEntity(SubServiceDTO dto) {
        SubService subService = new SubService();
        subService.setSubServiceName(dto.getSubServiceName());
        subService.setDescription(dto.getDescription());
        subService.setBasePrice(dto.getBasePrice());
        subService.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return subService;
    }

    // Get all subservices
    @GetMapping
    public ResponseEntity<List<SubServiceDTO>> getAllSubServices() {
        List<SubServiceDTO> dtos = subServiceService.getAllSubServices()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Get all subservices by service ID
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<SubServiceDTO>> getSubServicesByServiceId(@PathVariable Long serviceId) {
        List<SubServiceDTO> dtos = subServiceService.getSubServicesByServiceId(serviceId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Get subservice by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubServiceDTO> getSubServiceById(@PathVariable Long id) {
        SubService subService = subServiceService.getSubServiceById(id);
        return ResponseEntity.ok(convertToDTO(subService));
    }

    // Create new subservice
    @PostMapping("/service/{serviceId}")
    public ResponseEntity<SubServiceDTO> createSubService(@PathVariable Long serviceId,
                                                         @RequestBody SubServiceDTO dto) {
        SubService subService = convertToEntity(dto);
        SubService created = subServiceService.createSubService(serviceId, subService);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    // Update existing subservice
    @PutMapping("/{id}")
    public ResponseEntity<SubServiceDTO> updateSubService(@PathVariable Long id,
                                                         @RequestBody SubServiceDTO dto) {
        SubService subService = convertToEntity(dto);
        SubService updated = subServiceService.updateSubService(id, subService);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    // Delete subservice by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubService(@PathVariable Long id) {
        subServiceService.deleteSubService(id);
        return ResponseEntity.noContent().build();
    }
}
