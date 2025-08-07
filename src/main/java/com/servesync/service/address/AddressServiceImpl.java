package com.servesync.service.address;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;
import com.servesync.entity.address.Address;
import com.servesync.entity.user.User;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.address.AddressRepository;
import com.servesync.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper

    @Override
    public AddressResponseDTO addAddressForUser(Long userId, AddressRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Address address = modelMapper.map(dto, Address.class);
        address.setUser(user);

        Address saved = addressRepository.save(address);
        return modelMapper.map(saved, AddressResponseDTO.class); // Use ModelMapper
    }

    @Override
    public AddressResponseDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return modelMapper.map(address, AddressResponseDTO.class); // Use ModelMapper
    }

    @Override
    public List<AddressResponseDTO> getAllAddressesForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(address -> modelMapper.map(address, AddressResponseDTO.class)) // Use ModelMapper
                .toList();
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        modelMapper.map(dto, address); // Use ModelMapper for update
        Address updated = addressRepository.save(address);

        return modelMapper.map(updated, AddressResponseDTO.class); // Use ModelMapper
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
