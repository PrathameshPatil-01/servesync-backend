package com.servesync.repository.address;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servesync.entity.address.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByUserId(Long userId);
}
