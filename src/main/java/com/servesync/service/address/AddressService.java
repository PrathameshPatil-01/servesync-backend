package com.servesync.service.address;



import java.util.List;

import com.servesync.dto.address.AddressRequestDTO;
import com.servesync.dto.address.AddressResponseDTO;

public interface AddressService {

    AddressResponseDTO addAddressForUser(Long userId, AddressRequestDTO dto);

    AddressResponseDTO getAddressById(Long id);

    List<AddressResponseDTO> getAllAddressesForUser(Long userId);

    AddressResponseDTO updateAddress(Long id, AddressRequestDTO dto);

    String deleteAddress(Long id);
}
