package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceOfferCreateDTO;
import com.servesync.dto.provider.ProviderServiceOfferDTO;
import com.servesync.dto.provider.ProviderServiceOfferUpdateDTO;

import java.util.List;

public interface ProviderServiceOfferService {
    List<ProviderServiceOfferDTO> getProviderServiceOffers(Long providerId);
    ProviderServiceOfferDTO getProviderServiceOfferById(Long offerId);
    ProviderServiceOfferDTO addProviderServiceOffer(Long providerId, ProviderServiceOfferCreateDTO dto);
    ProviderServiceOfferDTO updateProviderServiceOffer(Long offerId, ProviderServiceOfferUpdateDTO dto);
    void deleteProviderServiceOffer(Long offerId);

    // Security helper method
    boolean isOfferOwnedByCurrentUser(Long offerId);
}
