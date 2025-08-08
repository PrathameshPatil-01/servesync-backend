package com.servesync.controller.service;

import com.servesync.dto.service.ServiceDTO;
import com.servesync.dto.service.ServiceWithSubServiceDTO;
import com.servesync.dto.service.SubServiceDTO;
import com.servesync.entity.service.Service;
import com.servesync.entity.service.SubService;
import com.servesync.service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Service service = serviceService.getServiceById(id);
        if (service == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(service);
    }
    
    @GetMapping("/{id}/subservices")
    public ResponseEntity<Set<SubServiceDTO>> getSubServiceByServiceId(@PathVariable Long id) {
        Set<SubServiceDTO> subServices = serviceService.getSubServicesByServiceId(id);
        return ResponseEntity.ok(subServices);
    }

    @PostMapping
    public ResponseEntity<String> createService(@RequestBody ServiceDTO service) {
        String created = serviceService.createService(service);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        Service updated = serviceService.updateService(id, service);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        boolean deleted = serviceService.deleteService(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-with-subservice")
    public ResponseEntity<String> addServiceWithSubService(@RequestBody ServiceWithSubServiceDTO dto) {
    	System.out.print("dto: ");
    	System.out.println(dto);
        String service = serviceService.addServiceWithSubService(dto);
        return ResponseEntity.ok(service);
    }
}
