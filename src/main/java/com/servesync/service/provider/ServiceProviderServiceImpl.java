package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderDetailsByServiceDTO;
import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceGetDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderGetDTO;
import com.servesync.dto.provider.ServiceProviderRequestDTO;
import com.servesync.dto.provider.ServiceProviderResponseDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;
import com.servesync.dto.provider.UserByServiceNameDTO;
import com.servesync.dto.provider.UserSubServiceDetailsDTO;
import com.servesync.entity.provider.ProviderService;
import com.servesync.entity.provider.ServiceProvider;
import com.servesync.entity.service.SubService;
import com.servesync.entity.user.User;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ServiceProviderRepository;
import com.servesync.repository.service.SubServiceRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.SecurityUtils;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private final ServiceProviderRepository serviceProviderRepository;
	private final SubServiceRepository subServiceRepository;
	private final UserRepository userRepo;
	private final SecurityUtils securityUtils;
	private final ModelMapper mapper;

	@Override
	public List<ServiceProviderResponseDTO> getAllProviders() {
		return serviceProviderRepository.findAll().stream()
				.map(provider -> mapper.map(provider, ServiceProviderResponseDTO.class)).collect(Collectors.toList());
	}

	@Override
	public ServiceProviderResponseDTO getProviderById(Long id) {
		ServiceProvider provider = serviceProviderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid ServiceProvider ID"));
		return mapper.map(provider, ServiceProviderResponseDTO.class);
	}

	@Override
	public ServiceProviderResponseDTO addProvider(ServiceProviderRequestDTO dto) {
	    Long currentUserId = securityUtils.getCurrentUserId();

	    // Check if user already has a provider
	    if (serviceProviderRepository.existsByUserId(currentUserId)) {
	        throw new ApiException("Provider already exists for this user");
	    }

	    User user = userRepo.findById(currentUserId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    ServiceProvider entity = mapper.map(dto, ServiceProvider.class);
	    entity.setUser(user);

	    ServiceProvider saved = serviceProviderRepository.save(entity);
	    return mapper.map(saved, ServiceProviderResponseDTO.class);
	}


	@Override
	public ServiceProviderResponseDTO updateProvider(Long id, ServiceProviderUpdateDTO dto) {
		ServiceProvider provider = serviceProviderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

		mapper.map(dto, provider); // Partial or full update
		ServiceProvider updated = serviceProviderRepository.save(provider);
		return mapper.map(updated, ServiceProviderResponseDTO.class);
	}

//		

	@Override
	public ProviderServiceDTO addProviderServiceToProvider(Long providerId, ProviderServiceCreateDTO dto) {

		ServiceProvider provider = serviceProviderRepository.findById(providerId)
				.orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));

		SubService subService = subServiceRepository.findById(dto.getSubServiceId()).orElseThrow(
				() -> new ResourceNotFoundException("SubService not found with ID: " + dto.getSubServiceId()));

		boolean alreadyExists = provider.getProviderServices().stream()
				.anyMatch(ps -> ps.getSubService().getId().equals(subService.getId()));
		if (alreadyExists) {
			throw new IllegalArgumentException("Provider already offers this sub-service.");
		}

		ProviderService providerService = mapper.map(dto, ProviderService.class);

		provider.addProviderService(providerService); // set bidirectional mapping

		// 5. Save provider (cascades providerService)
		serviceProviderRepository.save(provider);

		return mapper.map(providerService, ProviderServiceDTO.class);
	}

	@Override
	public List<ServiceProviderGetDTO> getAllProvidersWithServices() {
		return serviceProviderRepository.findAll().stream()
	            .map(provider -> {
	                // Use your new detailed DTO with nested services
	                ServiceProviderGetDTO dto = mapper.map(provider, ServiceProviderGetDTO.class);

	                // Map ProviderServices manually if needed (if mapper doesn't handle nested collections)
	                Set<ProviderServiceGetDTO> services = provider.getProviderServices().stream()
	                        .map(ps -> {
	                            ProviderServiceGetDTO serviceDTO = mapper.map(ps, ProviderServiceGetDTO.class);
	                            // Include subService name if needed
	                            serviceDTO.setSubServiceName(ps.getSubService().getSubServiceName());
	                            return serviceDTO;
	                        }).collect(Collectors.toSet());

	                dto.setProviderServices(services);
	                return dto;
	            }).collect(Collectors.toList());
	}

	@Override
	public List<ServiceProviderGetDTO> getProvidersBySubService(Long subServiceId) {
		return serviceProviderRepository.findAll().stream()
	            .filter(provider -> provider.getProviderServices().stream()
	                .anyMatch(ps -> ps.getSubService().getId().equals(subServiceId)))
	            .map(provider -> {
	                ServiceProviderGetDTO dto = mapper.map(provider, ServiceProviderGetDTO.class);

	                // Filter providerServices to include only the requested subservice
	                Set<ProviderServiceGetDTO> filteredServices = provider.getProviderServices().stream()
	                        .filter(ps -> ps.getSubService().getId().equals(subServiceId))
	                        .map(ps -> {
	                            ProviderServiceGetDTO serviceDTO = mapper.map(ps, ProviderServiceGetDTO.class);
	                            serviceDTO.setSubServiceName(ps.getSubService().getSubServiceName());
	                            return serviceDTO;
	                        }).collect(Collectors.toSet());

	                dto.setProviderServices(filteredServices);
	                return dto;
	            }).collect(Collectors.toList());
	}
	
	@Override
	public List<UserSubServiceDetailsDTO> getSubServicesByUserId(Long userId) {
	    ServiceProvider provider = serviceProviderRepository.findByUserId(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("ServiceProvider not found for user ID: " + userId));

	    return provider.getProviderServices().stream()
	            .map(ps -> {
	            	
	                UserSubServiceDetailsDTO dto = new UserSubServiceDetailsDTO();
	                dto.setServiceName(ps.getSubService().getService().getServiceName()); // Assuming SubService has getService()
	                dto.setSubServiceName(ps.getSubService().getSubServiceName());
	                dto.setSubServiceDescription(ps.getSubService().getDescription());
	                dto.setPrice(ps.getPrice());
	                dto.setCurrency(ps.getCurrency());
	                dto.setSubServiceId(ps.getSubService().getId());
	                
	                dto.setEstimatedDuration(ps.getEstimatedDuration());
	                return dto;
	            }).collect(Collectors.toList());
	}

	@Override
	public List<ProviderDetailsByServiceDTO> getProvidersByServiceName(String serviceName) {
	    return serviceProviderRepository.findAll().stream()
	        .flatMap(provider -> provider.getProviderServices().stream()
	            .filter(ps -> ps.getSubService().getService().getServiceName().equalsIgnoreCase(serviceName))
	            .map(ps -> {
	                ProviderDetailsByServiceDTO dto = new ProviderDetailsByServiceDTO();
	                dto.setProviderId(provider.getId());
	                dto.setFullName(provider.getFullName());
	                dto.setBusinessName(provider.getBusinessName());
	                dto.setServiceName(ps.getSubService().getService().getServiceName()); // ✅ Get actual service name
	               
	                dto.setEstimatedDuration(ps.getEstimatedDuration());
	                return dto;
	            })
	        )
	        .collect(Collectors.toList());
	}

	@Override
	public List<ProviderDetailsByServiceDTO> getAllUsersWithServices() {
	    return serviceProviderRepository.findAll().stream()
	        .flatMap(provider ->
	            provider.getProviderServices().stream()
	                .map(ps -> {
	                    ProviderDetailsByServiceDTO dto = new ProviderDetailsByServiceDTO();
	                    dto.setProviderId(provider.getId());
	                    dto.setFullName(provider.getFullName());
	                    dto.setBusinessName(provider.getBusinessName());
	                    dto.setServiceName(ps.getSubService().getService().getServiceName()); // ✅ Ensure getService() is not null
	                    dto.setEstimatedDuration(ps.getEstimatedDuration());
	                    return dto;
	                })
	        )
	        .collect(Collectors.toList());
	}

	
	
}
