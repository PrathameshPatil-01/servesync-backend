package com.servesync.controller.provider;

import com.servesync.dto.provider.ProviderServiceOfferCreateDTO;
import com.servesync.dto.provider.ProviderServiceOfferDTO;
import com.servesync.dto.provider.ProviderServiceOfferUpdateDTO;
import com.servesync.service.provider.ProviderServiceOfferService;
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
@RequestMapping("/api/provider-service-offers")
@RequiredArgsConstructor
@Validated
@Tag(name = "Provider Service Offers", description = "APIs for managing services offered by providers")
public class ProviderServiceOfferController {

    private final ProviderServiceOfferService providerServiceOfferService;

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get all service offers for a specific provider")
    public ResponseEntity<List<ProviderServiceOfferDTO>> getProviderServiceOffers(
            @PathVariable @Min(1) Long providerId) {
        List<ProviderServiceOfferDTO> list = providerServiceOfferService.getProviderServiceOffers(providerId);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{offerId}")
    @Operation(summary = "Get a specific service offer by ID")
    public ResponseEntity<ProviderServiceOfferDTO> getProviderServiceOfferById(@PathVariable @Min(1) Long offerId) {
        return ResponseEntity.ok(providerServiceOfferService.getProviderServiceOfferById(offerId));
    }

    @PostMapping("/provider/{providerId}")
    @Operation(summary = "Add a new service offer for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<ProviderServiceOfferDTO> addProviderServiceOffer(
            @PathVariable @Min(1) Long providerId,
            @Valid @RequestBody ProviderServiceOfferCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerServiceOfferService.addProviderServiceOffer(providerId, dto));
    }

    @PutMapping("/{offerId}")
    @Operation(summary = "Update an existing service offer")
    @PreAuthorize("hasRole('PROVIDER') and @providerServiceOfferService.isOfferOwnedByCurrentUser(#offerId) or hasRole('ADMIN')")
    public ResponseEntity<ProviderServiceOfferDTO> updateProviderServiceOffer(
            @PathVariable @Min(1) Long offerId,
            @Valid @RequestBody ProviderServiceOfferUpdateDTO dto) {
        return ResponseEntity.ok(providerServiceOfferService.updateProviderServiceOffer(offerId, dto));
    }

    @DeleteMapping("/{offerId}")
    @Operation(summary = "Delete a service offer")
    @PreAuthorize("hasRole('PROVIDER') and @providerServiceOfferService.isOfferOwnedByCurrentUser(#offerId) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProviderServiceOffer(@PathVariable @Min(1) Long offerId) {
        providerServiceOfferService.deleteProviderServiceOffer(offerId);
        return ResponseEntity.noContent().build();
    }
}
