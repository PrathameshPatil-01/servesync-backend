package com.servesync.service.address;

import com.servesync.dto.user.AddressRequestDTO;
import com.servesync.dto.user.AddressResponseDTO;
import com.servesync.entity.user.Address;
import com.servesync.entity.user.User;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.user.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // ✅ Helper method
    private AddressResponseDTO convertToDTO(Address address) {
        return modelMapper.map(address, AddressResponseDTO.class);
    }

    @Override
    public AddressResponseDTO addAddressForUser(Long userId, AddressRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Address address = modelMapper.map(dto, Address.class);
        address.setUser(user);

        Address saved = addressRepository.save(address);
        return convertToDTO(saved);
    }

    @Override
    public AddressResponseDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return convertToDTO(address);
    }

    @Override
    public List<AddressResponseDTO> getAllAddressesForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        modelMapper.map(dto, address); // ✅ Updates fields
        Address updated = addressRepository.save(address);

        return convertToDTO(updated);
    }

    @Override
    public String deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
        return "Address deleted successfully";
    }
}
