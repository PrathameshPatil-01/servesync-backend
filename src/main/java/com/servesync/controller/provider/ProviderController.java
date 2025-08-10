package com.servesync.controller.provider;

import com.servesync.dto.provider.ProviderDashboardStatsDTO;
import com.servesync.dto.provider.ProviderRequestDTO;
import com.servesync.dto.provider.ProviderResponseDTO;
import com.servesync.dto.provider.ProviderUpdateDTO;
import com.servesync.service.provider.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Provider Management", description = "APIs for managing service providers and their profiles")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    @Operation(summary = "Register a new service provider")
    public ResponseEntity<ProviderResponseDTO> registerProvider(@Valid @RequestBody ProviderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.registerProvider(dto));
    }

    @GetMapping
    @Operation(summary = "Get list of all service providers")
    public ResponseEntity<List<ProviderResponseDTO>> getAllProviders() {
        List<ProviderResponseDTO> list = providerService.getAllProviders();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{providerId}")
    @Operation(summary = "Get service provider details by ID")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROVIDER', 'ADMIN') and @providerService.isProviderAccessible(#providerId)")
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable @Min(1) Long providerId) {
        return ResponseEntity.ok(providerService.getProviderById(providerId));
    }

    @PutMapping("/{providerId}")
    @Operation(summary = "Update service provider details")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<ProviderResponseDTO> updateProvider(
            @PathVariable @Min(1) Long providerId,
            @Valid @RequestBody ProviderUpdateDTO dto) {
        return ResponseEntity.ok(providerService.updateProvider(providerId, dto));
    }

    @DeleteMapping("/{providerId}")
    @Operation(summary = "Delete service provider by ID")
    @PreAuthorize("hasRole('ADMIN')") // Only admin can delete providers
    public ResponseEntity<Void> deleteProvider(@PathVariable @Min(1) Long providerId) {
        providerService.deleteProvider(providerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current")
    @Operation(summary = "Get current authenticated provider's profile")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ProviderResponseDTO> getCurrentProviderProfile() {
        return ResponseEntity.ok(providerService.getCurrentProviderProfile());
    }

    @GetMapping("/{providerId}/dashboard-stats")
    @Operation(summary = "Get dashboard statistics for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<ProviderDashboardStatsDTO> getProviderDashboardStats(@PathVariable @Min(1) Long providerId) {
        return ResponseEntity.ok(providerService.getProviderDashboardStats(providerId));
    }
}
