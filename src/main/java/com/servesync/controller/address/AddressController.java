package com.servesync.controller.address;

import com.servesync.dto.user.*;
import com.servesync.dto.address.*;
import com.servesync.dto.booking.*;
import com.servesync.dto.payment.*;
import com.servesync.dto.review.*;
import com.servesync.dto.provider.*;
import com.servesync.dto.verification.*;
import com.servesync.dto.audit.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all addresses of a user")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/user/{userId}")
    @Operation(summary = "Add address for a user")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable Long userId, @RequestBody @Valid AddressRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new AddressResponse());
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update address")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId, @RequestBody @Valid AddressRequest dto) {
        return ResponseEntity.ok(new AddressResponse());
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete address")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok("Address deleted");
    }
}
