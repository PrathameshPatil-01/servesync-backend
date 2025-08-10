package com.servesync.service.earning;

import com.servesync.dto.earning.EarningSummaryDTO;
import com.servesync.dto.earning.PayoutRequestDTO;
import com.servesync.dto.earning.PayoutResponseDTO;
import com.servesync.dto.earning.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EarningService {
    EarningSummaryDTO getEarningsSummary(Long providerId);
    Page<TransactionResponseDTO> getTransactions(Long providerId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    PayoutResponseDTO requestPayout(Long providerId, PayoutRequestDTO dto);
    Page<PayoutResponseDTO> getPayoutHistory(Long providerId, Pageable pageable);
}
