package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceOfferCreateDTO;
import com.servesync.dto.provider.ProviderServiceOfferDTO;
import com.servesync.dto.provider.ProviderServiceOfferUpdateDTO;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.provider.ProviderServiceOffer;
import com.servesync.entity.service.SubService;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ProviderRepository;
import com.servesync.repository.provider.ProviderServiceOfferRepository;
import com.servesync.repository.service.SubServiceRepository;
import com.servesync.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.modelmapper.PropertyMap; // Import PropertyMap
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceOfferServiceImpl implements ProviderServiceOfferService {

    private final ProviderServiceOfferRepository providerServiceOfferRepository;
    private final ProviderRepository providerRepository;
    private final SubServiceRepository subServiceRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper
    private final SecurityUtils securityUtils;

    @Override
    public List<ProviderServiceOfferDTO> getProviderServiceOffers(Long providerId) {
        return providerServiceOfferRepository.findByProviderId(providerId).stream()
                .map(offer -> modelMapper.map(offer, ProviderServiceOfferDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public ProviderServiceOfferDTO getProviderServiceOfferById(Long offerId) {
        ProviderServiceOffer offer = providerServiceOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service Offer not found with ID: " + offerId));
        return modelMapper.map(offer, ProviderServiceOfferDTO.class); // Use ModelMapper
    }

    @Override
    public ProviderServiceOfferDTO addProviderServiceOffer(Long providerId, ProviderServiceOfferCreateDTO dto) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

        SubService subService = subServiceRepository.findById(dto.getSubServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("SubService not found with ID: " + dto.getSubServiceId()));

        // Check if provider already offers this sub-service
        boolean alreadyExists = provider.getServiceOffers().stream()
                .anyMatch(pso -> pso.getSubService().getId().equals(subService.getId()) && !pso.getIsDeleted());
        if (alreadyExists) {
            throw new ApiException("Provider already offers this sub-service.");
        }

        ProviderServiceOffer offer = modelMapper.map(dto, ProviderServiceOffer.class); // Use ModelMapper
        offer.setProvider(provider);
        offer.setSubService(subService);

        ProviderServiceOffer savedOffer = providerServiceOfferRepository.save(offer);
        log.info("Provider Service Offer created with ID: {}", savedOffer.getId());
        return modelMapper.map(savedOffer, ProviderServiceOfferDTO.class); // Use ModelMapper
    }

    @Override
    public ProviderServiceOfferDTO updateProviderServiceOffer(Long offerId, ProviderServiceOfferUpdateDTO dto) {
        ProviderServiceOffer existingOffer = providerServiceOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service Offer not found with ID: " + offerId));

        modelMapper.map(dto, existingOffer); // Use ModelMapper for update
        ProviderServiceOffer updatedOffer = providerServiceOfferRepository.save(existingOffer);
        log.info("Provider Service Offer ID {} updated.", offerId);
        return modelMapper.map(updatedOffer, ProviderServiceOfferDTO.class); // Use ModelMapper
    }

    @Override
    public void deleteProviderServiceOffer(Long offerId) {
        ProviderServiceOffer offer = providerServiceOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service Offer not found with ID: " + offerId));
        offer.setIsDeleted(true); // Soft delete
        providerServiceOfferRepository.save(offer);
        log.info("Provider Service Offer ID {} soft-deleted.", offerId);
    }

    @Override
    public boolean isOfferOwnedByCurrentUser(Long offerId) {
        Long currentProviderId = securityUtils.getCurrentProviderId();
        return providerServiceOfferRepository.existsByIdAndProviderId(offerId, currentProviderId);
    }
}
