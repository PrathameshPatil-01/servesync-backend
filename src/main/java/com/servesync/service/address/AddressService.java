package com.servesync.service.address;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;

public interface AddressService {
    AddressResponseDTO getAddressByUserId(Long userId);
    AddressResponseDTO addOrUpdateAddress(Long userId, AddressRequestDTO requestDTO);
}