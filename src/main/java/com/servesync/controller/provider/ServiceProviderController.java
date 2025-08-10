package com.servesync.controller.provider;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.servesync.dto.provider.ProviderDetailsByServiceDTO;
import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderGetDTO;
import com.servesync.dto.provider.ServiceProviderRequestDTO;
import com.servesync.dto.provider.ServiceProviderResponseDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;
import com.servesync.dto.provider.UserSubServiceDetailsDTO;
import com.servesync.service.provider.ServiceProviderService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/service-providers")
@CrossOrigin(origins = "http://localhost:5173")  // adjust origin if needed
@Validated
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;
    
    public ServiceProviderController(ServiceProviderService service) {
        this.serviceProviderService = service;
    }

    @PostMapping 
    @PreAuthorize("hasRole('PROVIDER') or hasRole('ADMIN')")
    @Operation(description = "Add a new service provider")
    public ResponseEntity<ServiceProviderResponseDTO> createProvider(
            @Valid @RequestBody ServiceProviderRequestDTO dto) {

        ServiceProviderResponseDTO response = serviceProviderService.addProvider(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(description = "Get list of all service providers")
    public ResponseEntity<?> getAllProviders() {
        List<ServiceProviderResponseDTO> list = serviceProviderService.getAllProviders();
        if (list.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get service provider details by ID")
    public ResponseEntity<?> getProviderById(@PathVariable Long id) {
        ServiceProviderResponseDTO dto = serviceProviderService.getProviderById(id);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    @Operation(description = "Update service provider details")
    public ResponseEntity<?> updateProvider(@PathVariable Long id,
                                            @Valid @RequestBody ServiceProviderUpdateDTO dto) {
        ServiceProviderResponseDTO updated = serviceProviderService.updateProvider(id, dto);
        return ResponseEntity.ok(updated);
    }

//    @DeleteMapping("/{id}")
//    @Operation(description = "Delete service provider by ID")
//    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
//        serviceProviderService.deleteProvider(id);
//        return ResponseEntity.ok("Service provider deleted successfully");
//    }

   
    
    @GetMapping("/with-services")
    @Operation(description = "Get all service providers along with their services")
    public ResponseEntity<List<ServiceProviderGetDTO>> getAllProvidersWithServices() {
        List<ServiceProviderGetDTO> providers = serviceProviderService.getAllProvidersWithServices();
        if (providers.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(providers);
    }

    // ✅ Get all providers offering a specific subservice
    @GetMapping("/by-subservice/{subServiceId}")
    @Operation(description = "Get providers who offer a specific sub-service by ID")
    public ResponseEntity<List<ServiceProviderGetDTO>> getProvidersBySubService(
            @PathVariable Long subServiceId) {

        List<ServiceProviderGetDTO> providers = serviceProviderService.getProvidersBySubService(subServiceId);
        if (providers.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(providers);

}
    
    @GetMapping("/users/{userId}/sub-services")
    public ResponseEntity<List<UserSubServiceDetailsDTO>> getUserSubServices(@PathVariable Long userId) {
        List<UserSubServiceDetailsDTO> services = serviceProviderService.getSubServicesByUserId(userId);
        return services.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(services);
    }
    
    @GetMapping("/by-service-name")
    public ResponseEntity<List<ProviderDetailsByServiceDTO>> getByServiceName(@RequestParam String serviceName) {
        List<ProviderDetailsByServiceDTO> providers = serviceProviderService.getProvidersByServiceName(serviceName);
        if (providers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/all-users-with-services")
    public ResponseEntity<List<ProviderDetailsByServiceDTO>> getAllUsersWithServices() {
        List<ProviderDetailsByServiceDTO> result = serviceProviderService.getAllUsersWithServices();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }


}
