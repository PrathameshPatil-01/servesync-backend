package com.servesync.controller.address;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.security.CustomUserDetails;
import com.servesync.security.SecurityUtils;
import com.servesync.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final SecurityUtils securityUtils;

    // Get Address of logged-in user
    @GetMapping
    public ResponseEntity<AddressResponseDTO> getAddress() {
        Long userId = securityUtils.getCurrentUserId();
        AddressResponseDTO response = addressService.getAddressByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Add or Update Address of logged-in user
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addOrUpdateAddress(@RequestBody AddressRequestDTO requestDTO) {
        Long userId = securityUtils.getCurrentUserId();
        AddressResponseDTO response = addressService.addOrUpdateAddress(userId, requestDTO);
        return ResponseEntity.ok(response);
    }

}
