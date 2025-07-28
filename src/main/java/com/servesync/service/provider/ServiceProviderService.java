package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderCreateDTO;
import com.servesync.dto.provider.ServiceProviderDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;

import java.util.List;

public interface ServiceProviderService {

	ProviderServiceDTO addProviderServiceToProvider(Long providerId, ProviderServiceCreateDTO dto);

	List<ServiceProviderDTO> getAllProviders();

	ServiceProviderDTO getProviderById(Long id);

	ServiceProviderDTO addProvider(ServiceProviderCreateDTO dto);

	ServiceProviderDTO updateProvider(Long id, ServiceProviderUpdateDTO dto);

	void deleteProvider(Long id);
}
