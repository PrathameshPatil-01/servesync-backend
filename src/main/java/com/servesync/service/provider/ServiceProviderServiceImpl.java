package com.servesync.service.provider;

import com.servesync.dto.provider.*;
import com.servesync.entity.provider.*;
import com.servesync.entity.service.SubService;
import com.servesync.entity.user.User;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ServiceProviderRepository;
import com.servesync.repository.service.SubServiceRepository;
import com.servesync.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
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
    public List<ServiceProviderDTO> getAllProviders() {
        return serviceProviderRepository.findAll().stream().map(provider -> {
            ServiceProviderDTO dto = mapper.map(provider, ServiceProviderDTO.class);
            if (provider.getProfileImage() != null) {
                dto.setProfileImage(Base64.getEncoder().encodeToString(provider.getProfileImage()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ServiceProviderDTO getProviderById(Long id) {
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid ServiceProvider ID"));

        ServiceProviderDTO dto = mapper.map(provider, ServiceProviderDTO.class);
        if (provider.getProfileImage() != null) {
            dto.setProfileImage(Base64.getEncoder().encodeToString(provider.getProfileImage()));
        }
        return dto;
    }

    @Override
    public ServiceProviderDTO addProvider(ServiceProviderCreateDTO dto) {
        if (serviceProviderRepository.existsByUserId(dto.getUserId())) {
            throw new ApiException("Provider already exists for this user");
        }

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ServiceProvider entity = mapper.map(dto, ServiceProvider.class);
        entity.setUser(user);

        if (dto.getProfileImage() != null) {
            entity.setProfileImage(Base64.getDecoder().decode(dto.getProfileImage()));
        }

        ServiceProvider saved = serviceProviderRepository.save(entity);

        ServiceProviderDTO responseDTO = mapper.map(saved, ServiceProviderDTO.class);
        if (saved.getProfileImage() != null) {
            responseDTO.setProfileImage(Base64.getEncoder().encodeToString(saved.getProfileImage()));
        }
        return responseDTO;
    }

    @Override
    public ServiceProviderDTO updateProvider(Long id, ServiceProviderUpdateDTO dto) {
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        mapper.map(dto, provider);

        if (dto.getProfileImage() != null) {
            provider.setProfileImage(Base64.getDecoder().decode(dto.getProfileImage()));
        }

        ServiceProvider updated = serviceProviderRepository.save(provider);
        ServiceProviderDTO responseDTO = mapper.map(updated, ServiceProviderDTO.class);
        if (updated.getProfileImage() != null) {
            responseDTO.setProfileImage(Base64.getEncoder().encodeToString(updated.getProfileImage()));
        }
        return responseDTO;
    }

    @Override
    public void deleteProvider(Long id) {
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        serviceProviderRepository.delete(provider);
    }

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
        provider.addProviderService(providerService);

        serviceProviderRepository.save(provider);

        return mapper.map(providerService, ProviderServiceDTO.class);
    }
}
