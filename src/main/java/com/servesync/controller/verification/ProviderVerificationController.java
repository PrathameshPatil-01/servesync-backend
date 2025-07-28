package com.servesync.controller.verification;

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
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
@Validated
public class ProviderVerificationController {

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get verifications for a provider")
    public ResponseEntity<List<ProviderVerificationResponse>> getVerifications(@PathVariable Long providerId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/provider/{providerId}")
    @Operation(summary = "Submit verification for a provider")
    public ResponseEntity<ProviderVerificationResponse> submitVerification(@PathVariable Long providerId, @RequestBody @Valid ProviderVerificationRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProviderVerificationResponse());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update verification status")
    public ResponseEntity<String> updateVerificationStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok("Verification status updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete verification record")
    public ResponseEntity<String> deleteVerification(@PathVariable Long id) {
        return ResponseEntity.ok("Verification deleted");
    }
}