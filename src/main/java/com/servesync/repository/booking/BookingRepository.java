package com.servesync.repository.booking;

import com.servesync.entity.booking.Booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	Optional<Booking> findByIdAndIsDeletedFalse(Long id);
    List<Booking> findAllByIsDeletedFalse();
}


