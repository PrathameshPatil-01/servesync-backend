package com.servesync.service.provider;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.servesync.dto.provider.ProviderServiceCreateDTO;
import com.servesync.dto.provider.ProviderServiceDTO;
import com.servesync.dto.provider.ProviderServiceUpdateDTO;
import com.servesync.entity.provider.ProviderService;
import com.servesync.entity.provider.ServiceProvider;
import com.servesync.entity.service.SubService;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ProviderServiceRepository;
import com.servesync.repository.provider.ServiceProviderRepository;
import com.servesync.repository.service.SubServiceRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProviderServiceServiceImpl implements ProviderServiceService {

    private final ProviderServiceRepository providerServiceRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final SubServiceRepository subServiceRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProviderServiceDTO> getAll() {
        return providerServiceRepository.findAll()
                .stream()
                .map(ps -> modelMapper.map(ps, ProviderServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProviderServiceDTO getById(Long id) {
        ProviderService providerService = providerServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProviderService not found with id: " + id));
        return modelMapper.map(providerService, ProviderServiceDTO.class);
    }

    @Override
    public ProviderServiceDTO create(ProviderServiceCreateDTO createDto) {
        ServiceProvider provider = serviceProviderRepository.findById(createDto.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("ServiceProvider not found with id: " + createDto.getProviderId()));

        SubService subService = subServiceRepository.findById(createDto.getSubServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("SubService not found with id: " + createDto.getSubServiceId()));

        ProviderService providerService = modelMapper.map(createDto, ProviderService.class);
        providerService.setProvider(provider);
        providerService.setSubService(subService);

        // Set default for isActive if null
        if (providerService.getIsActive() == null) {
            providerService.setIsActive(true);
        }

        ProviderService saved = providerServiceRepository.save(providerService);
        return modelMapper.map(saved, ProviderServiceDTO.class);
    }

    @Override
    public ProviderServiceDTO update(Long id, ProviderServiceUpdateDTO updateDto) {
        ProviderService providerService = providerServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProviderService not found with id: " + id));

        // ModelMapper will copy only non-null & valid values based on your config
        modelMapper.map(updateDto, providerService);

        ProviderService updated = providerServiceRepository.save(providerService);
        return modelMapper.map(updated, ProviderServiceDTO.class);
    }

    @Override
    public void delete(Long id) {
        ProviderService providerService = providerServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProviderService not found with id: " + id));
        providerServiceRepository.delete(providerService);
    }
}

