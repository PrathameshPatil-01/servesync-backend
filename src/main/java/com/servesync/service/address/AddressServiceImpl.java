package com.servesync.service.address;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.entity.address.Address;
import com.servesync.entity.user.User;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.address.AddressRepository;
import com.servesync.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressResponseDTO getAddressByUserId(Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No address found for user ID: " + userId));
        return modelMapper.map(address, AddressResponseDTO.class);
    }

    @Override
    public AddressResponseDTO addOrUpdateAddress(Long userId, AddressRequestDTO requestDTO) {
        // Fetch the user (throws exception if not found)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Find existing address or create a new one
        Address address = addressRepository.findByUserId(userId).orElse(new Address());
        address.setUser(user);

        // Map request fields to entity
        modelMapper.map(requestDTO, address);

        // Save the address (works for both insert & update)
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressResponseDTO.class);
    }
}
