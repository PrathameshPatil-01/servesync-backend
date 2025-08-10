package com.servesync.controller.earning;

import com.servesync.dto.earning.EarningSummaryDTO;
import com.servesync.dto.earning.PayoutRequestDTO;
import com.servesync.dto.earning.PayoutResponseDTO;
import com.servesync.dto.earning.TransactionResponseDTO;
import com.servesync.service.earning.EarningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/earnings")
@RequiredArgsConstructor
@Validated
@Tag(name = "Provider Earnings & Payouts", description = "APIs for managing provider's earnings and payout requests")
public class EarningController {

    private final EarningService earningService;

    @GetMapping("/provider/{providerId}/summary")
    @Operation(summary = "Get earnings summary for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<EarningSummaryDTO> getEarningsSummary(@PathVariable @Min(1) Long providerId) {
        return ResponseEntity.ok(earningService.getEarningsSummary(providerId));
    }

    @GetMapping("/provider/{providerId}/transactions")
    @Operation(summary = "Get transaction history for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
            @PathVariable @Min(1) Long providerId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable) {
        return ResponseEntity.ok(earningService.getTransactions(providerId, startDate, endDate, pageable));
    }

    @PostMapping("/provider/{providerId}/payout-requests")
    @Operation(summary = "Request a payout for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId")
    public ResponseEntity<PayoutResponseDTO> requestPayout(
            @PathVariable @Min(1) Long providerId,
            @Valid @RequestBody PayoutRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(earningService.requestPayout(providerId, dto));
    }

    @GetMapping("/provider/{providerId}/payout-history")
    @Operation(summary = "Get payout history for a provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<Page<PayoutResponseDTO>> getPayoutHistory(
            @PathVariable @Min(1) Long providerId,
            Pageable pageable) {
        return ResponseEntity.ok(earningService.getPayoutHistory(providerId, pageable));
    }
}
