package com.servesync.controller.address;

import com.servesync.dto.user.AddressRequestDTO;
import com.servesync.dto.user.AddressResponseDTO;
import com.servesync.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {   // ✅ Class must be public

    private final AddressService addressService; // ✅ final field

    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable Long userId,
            @RequestBody AddressRequestDTO dto) {

        return ResponseEntity.ok(addressService.addAddressForUser(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAllAddressesForUser(userId));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequestDTO dto) {

        return ResponseEntity.ok(addressService.updateAddress(addressId, dto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.deleteAddress(addressId));
    }
}
