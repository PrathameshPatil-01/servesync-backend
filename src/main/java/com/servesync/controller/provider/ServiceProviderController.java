package com.servesync.controller.provider;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderCreateDTO;
import com.servesync.dto.provider.ServiceProviderDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;
import com.servesync.service.provider.ServiceProviderService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "http://localhost:3000")  // adjust origin if needed
@AllArgsConstructor
@Validated
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @GetMapping
    @Operation(description = "Get list of all service providers")
    public ResponseEntity<?> getAllProviders() {
        List<ServiceProviderDTO> list = serviceProviderService.getAllProviders();
        if (list.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get service provider details by ID")
    public ResponseEntity<?> getProviderById(@PathVariable Long id) {
        ServiceProviderDTO dto = serviceProviderService.getProviderById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(description = "Add a new service provider")
    public ResponseEntity<?> addProvider(@Valid @RequestBody ServiceProviderCreateDTO dto) {
        ServiceProviderDTO created = serviceProviderService.addProvider(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update service provider details")
    public ResponseEntity<?> updateProvider(@PathVariable Long id,
                                            @Valid @RequestBody ServiceProviderUpdateDTO dto) {
        ServiceProviderDTO updated = serviceProviderService.updateProvider(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete service provider by ID")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        serviceProviderService.deleteProvider(id);
        return ResponseEntity.ok("Service provider deleted successfully");
    }

    @PostMapping("/{providerId}/services")
    public ResponseEntity<ProviderServiceDTO> addProviderService(
            @PathVariable Long providerId,
            @Valid @RequestBody ProviderServiceCreateDTO dto) {
        ProviderServiceDTO created = serviceProviderService.addProviderServiceToProvider(providerId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}
