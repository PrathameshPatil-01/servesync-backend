package com.servesync.controller.address;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.security.CustomUserDetails;
import com.servesync.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // Get Address of logged-in user
    @GetMapping
    public ResponseEntity<AddressResponseDTO> getAddress() {
        Long userId = getLoggedInUserId();
        AddressResponseDTO response = addressService.getAddressByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Add or Update Address of logged-in user
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addOrUpdateAddress(@RequestBody AddressRequestDTO requestDTO) {
        Long userId = getLoggedInUserId();
        AddressResponseDTO response = addressService.addOrUpdateAddress(userId, requestDTO);
        return ResponseEntity.ok(response);
    }

    // Helper method to extract user ID from token
    private Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        }

        throw new RuntimeException("User not authenticated");
    }
}
