package com.servesync.service.earning;

import com.servesync.dto.earning.EarningSummaryDTO;
import com.servesync.dto.earning.PayoutRequestDTO;
import com.servesync.dto.earning.PayoutResponseDTO;
import com.servesync.dto.earning.TransactionResponseDTO;
import com.servesync.entity.earning.Earning;
import com.servesync.entity.earning.Payout;
import com.servesync.entity.provider.Provider;
import com.servesync.enums.EarningTypeEnum;
import com.servesync.enums.PayoutStatusEnum;
import com.servesync.enums.TransactionTypeEnum; // Added for constant mapping
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.earning.EarningRepository;
import com.servesync.repository.earning.PayoutRepository;
import com.servesync.repository.provider.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.modelmapper.PropertyMap; // Import PropertyMap for custom mappings
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl; // Import PageImpl
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EarningServiceImpl implements EarningService {

    private final EarningRepository earningRepository;
    private final PayoutRepository payoutRepository;
    private final ProviderRepository providerRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper

    // Initialize ModelMapper configurations in the constructor
    // This ensures mappings are set up once when the bean is created
    public EarningServiceImpl(EarningRepository earningRepository, PayoutRepository payoutRepository,
                              ProviderRepository providerRepository, ModelMapper modelMapper) {
        this.earningRepository = earningRepository;
        this.payoutRepository = payoutRepository;
        this.providerRepository = providerRepository;
        this.modelMapper = modelMapper;

        // Configure mapping for Earning to TransactionResponseDTO
        modelMapper.addMappings(new PropertyMap<Earning, TransactionResponseDTO>() {
            @Override
            protected void configure() {
                map().setProviderId(source.getProvider().getId());
                map().setRelatedEntityId(source.getOrder() != null ? source.getOrder().getId() : null);
                map().setType(TransactionTypeEnum.EARNING); // Constant mapping
                map().setTransactionDate(source.getEarningDate());
            }
        });

        // Configure mapping for Payout to PayoutResponseDTO
        modelMapper.addMappings(new PropertyMap<Payout, PayoutResponseDTO>() {
            @Override
            protected void configure() {
                map().setProviderId(source.getProvider().getId());
            }
        });
    }

    @Override
    public EarningSummaryDTO getEarningsSummary(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        // Calculate earnings for today, this week, this month, and total
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // Monday
        LocalDate startOfMonth = today.withDayOfMonth(1);

        BigDecimal todayEarnings = earningRepository.sumEarningsByProviderAndDateRange(providerId, today.atStartOfDay(), today.atTime(LocalTime.MAX));
        BigDecimal thisWeekEarnings = earningRepository.sumEarningsByProviderAndDateRange(providerId, startOfWeek.atStartOfDay(), now);
        BigDecimal thisMonthEarnings = earningRepository.sumEarningsByProviderAndDateRange(providerId, startOfMonth.atStartOfDay(), now);
        BigDecimal totalEarnings = earningRepository.sumTotalEarningsByProvider(providerId);

        // Calculate available for payout (total earnings - processed payouts)
        BigDecimal processedPayouts = payoutRepository.sumPayoutsByProviderAndStatuses(providerId, List.of(PayoutStatusEnum.COMPLETED));
        BigDecimal availableForPayout = totalEarnings.subtract(processedPayouts);

        // Calculate pending payouts
        BigDecimal pendingPayouts = payoutRepository.sumPayoutsByProviderAndStatuses(providerId, List.of(PayoutStatusEnum.PENDING, PayoutStatusEnum.PROCESSING));

        return EarningSummaryDTO.builder()
                .todayEarnings(todayEarnings)
                .thisWeekEarnings(thisWeekEarnings)
                .thisMonthEarnings(thisMonthEarnings)
                .totalEarnings(totalEarnings)
                .availableForPayout(availableForPayout)
                .pendingPayouts(pendingPayouts)
                .build();
    }

    @Override
    public Page<TransactionResponseDTO> getTransactions(Long providerId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : LocalDateTime.MAX;

        // Fetch earnings
        Page<Earning> earningsPage = earningRepository.findByProviderIdAndEarningDateBetween(providerId, startDateTime, endDateTime, pageable);

        List<TransactionResponseDTO> earningTransactions = earningsPage.getContent().stream()
                .map(earning -> modelMapper.map(earning, TransactionResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(earningTransactions, pageable, earningsPage.getTotalElements());
    }

    @Override
    public PayoutResponseDTO requestPayout(Long providerId, PayoutRequestDTO dto) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        // Check if requested amount is available for payout
        EarningSummaryDTO summary = getEarningsSummary(providerId);
        if (dto.getAmount().compareTo(summary.getAvailableForPayout()) > 0) {
            throw new ApiException("Requested payout amount exceeds available balance.");
        }

        Payout payout = modelMapper.map(dto, Payout.class);
        payout.setProvider(provider);
        payout.setStatus(PayoutStatusEnum.PENDING); // Initial status
        payout.setRequestedAt(LocalDateTime.now());
        // transactionId and processedAt will be set upon actual processing by admin/system

        Payout savedPayout = payoutRepository.save(payout);
        log.info("Payout request created for provider ID: {} with amount: {}", providerId, dto.getAmount());
        return modelMapper.map(savedPayout, PayoutResponseDTO.class);
    }

    @Override
    public Page<PayoutResponseDTO> getPayoutHistory(Long providerId, Pageable pageable) {
        providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        Page<Payout> payoutsPage = payoutRepository.findByProviderId(providerId, pageable);

        List<PayoutResponseDTO> payoutHistory = payoutsPage.getContent().stream()
                .map(payout -> modelMapper.map(payout, PayoutResponseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(payoutHistory, pageable, payoutsPage.getTotalElements());
    }
}
