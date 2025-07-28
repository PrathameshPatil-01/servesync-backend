package com.servesync.service.address;

public interface AddressService {
    List<AddressResponse> getAddressesByUser(Long userId);
    AddressResponse addAddress(Long userId, AddressRequest dto);
    AddressResponse updateAddress(Long addressId, AddressRequest dto);
    void deleteAddress(Long addressId);
}
