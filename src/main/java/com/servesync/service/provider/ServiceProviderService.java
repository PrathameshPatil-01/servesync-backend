package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderResponseDTO;
import com.servesync.dto.provider.ServiceProviderRequestDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;

import java.util.List;

public interface ServiceProviderService {

	ProviderServiceDTO addProviderServiceToProvider(Long providerId, ProviderServiceCreateDTO dto);

	List<ServiceProviderResponseDTO> getAllProviders();

	ServiceProviderResponseDTO getProviderById(Long id);

	ServiceProviderResponseDTO addProvider(ServiceProviderRequestDTO dto);

	ServiceProviderResponseDTO updateProvider(Long id, ServiceProviderUpdateDTO dto);

}
