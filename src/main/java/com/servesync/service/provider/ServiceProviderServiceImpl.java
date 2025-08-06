package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ServiceProviderRequestDTO;
import com.servesync.dto.provider.ServiceProviderResponseDTO;
import com.servesync.dto.provider.ServiceProviderUpdateDTO;
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
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private final ServiceProviderRepository serviceProviderRepository;
	private final SubServiceRepository subServiceRepository;
	private final UserRepository userRepo;
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
	    Long currentUserId = SecurityUtils.getCurrentUserId();

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

//	@Override
//	public void deleteProvider(Long id) {
//		ServiceProvider provider = serviceProviderRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
//		serviceProviderRepository.delete(provider); // Hard delete (change to soft if needed)
//	}

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
}
