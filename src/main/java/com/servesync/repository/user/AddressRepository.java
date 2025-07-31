package com.servesync.repository.user;

import com.servesync.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId); // ✅ Required
}
