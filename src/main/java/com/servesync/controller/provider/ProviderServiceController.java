package com.servesync.controller.provider;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ProviderServiceUpdateDTO;
import com.servesync.service.provider.ProviderServiceService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider-services")
@Validated
@AllArgsConstructor
public class ProviderServiceController {

    private final ProviderServiceService providerServiceService;

    @GetMapping
    public ResponseEntity<List<ProviderServiceDTO>> getAll() {
        List<ProviderServiceDTO> list = providerServiceService.getAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderServiceDTO> getById(@PathVariable Long id) {
        ProviderServiceDTO dto = providerServiceService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProviderServiceDTO> create(@Valid @RequestBody ProviderServiceCreateDTO dto) {
        ProviderServiceDTO created = providerServiceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderServiceDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody ProviderServiceUpdateDTO dto) {
        ProviderServiceDTO updated = providerServiceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        providerServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
