package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderDetailsByServiceDTO;
import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderGetDTO;
import com.servesync.dto.provider.ServiceProviderResponseDTO;
import com.servesync.dto.provider.ServiceProviderRequestDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;
import com.servesync.dto.provider.UserByServiceNameDTO;
import com.servesync.dto.provider.UserSubServiceDetailsDTO;

import java.util.List;

public interface ServiceProviderService {

	
	ProviderServiceDTO addProviderServiceToProvider(Long providerId, ProviderServiceCreateDTO dto);

	List<ServiceProviderResponseDTO> getAllProviders();

	ServiceProviderResponseDTO getProviderById(Long id);

	ServiceProviderResponseDTO addProvider(ServiceProviderRequestDTO dto);

	ServiceProviderResponseDTO updateProvider(Long id, ServiceProviderUpdateDTO dto);
	 List<ServiceProviderGetDTO> getAllProvidersWithServices();

	    List<ServiceProviderGetDTO> getProvidersBySubService(Long subServiceId);
	    List<UserSubServiceDetailsDTO> getSubServicesByUserId(Long userId);
	    List<ProviderDetailsByServiceDTO> getProvidersByServiceName(String serviceName);
	    List<ProviderDetailsByServiceDTO> getAllUsersWithServices();
	   

}
