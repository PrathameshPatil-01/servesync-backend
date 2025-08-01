package com.servesync.repository.address;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.address.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId); // ✅ Required
}
