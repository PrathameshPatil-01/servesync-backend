package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderDashboardStatsDTO;
import com.servesync.dto.provider.ProviderDetailsByServiceDTO;
import com.servesync.dto.provider.ProviderRequestDTO;
import com.servesync.dto.provider.ProviderResponseDTO;
import com.servesync.dto.provider.ProviderUpdateDTO;
import com.servesync.dto.provider.ServiceProviderGetDTO;
import com.servesync.dto.provider.UserSubServiceDetailsDTO;

import java.util.List;

public interface ProviderService {
    ProviderResponseDTO registerProvider(ProviderRequestDTO dto);
    List<ProviderResponseDTO> getAllProviders();
    ProviderResponseDTO getProviderById(Long id);
    ProviderResponseDTO updateProvider(Long id, ProviderUpdateDTO dto);
    void deleteProvider(Long id);
    ProviderResponseDTO getCurrentProviderProfile();
    ProviderDashboardStatsDTO getProviderDashboardStats(Long providerId);

    // Security helper methods
    boolean isProviderRegisteredForCurrentUser();
    boolean isProviderAccessible(Long providerId);

	 List<ServiceProviderGetDTO> getAllProvidersWithServices();
    List<ServiceProviderGetDTO> getProvidersBySubService(Long subServiceId);
    List<UserSubServiceDetailsDTO> getSubServicesByUserId(Long userId);
    List<ProviderDetailsByServiceDTO> getProvidersByServiceName(String serviceName);
    List<ProviderDetailsByServiceDTO> getAllUsersWithServices();
}
