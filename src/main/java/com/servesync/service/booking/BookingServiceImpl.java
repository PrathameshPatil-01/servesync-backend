package com.servesync.service.booking;

import com.servesync.dto.booking.BookingRequestDTO;
import com.servesync.dto.booking.BookingResponseDTO;
import com.servesync.entity.booking.Booking;
import com.servesync.enums.BookingStatusEnum;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.address.AddressRepository;
import com.servesync.repository.booking.BookingRepository;
import com.servesync.repository.provider.ProviderServiceRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.service.booking.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProviderServiceRepository providerServiceRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        Booking booking = modelMapper.map(dto, Booking.class);
        booking.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found")));
        booking.setProviderService(providerServiceRepository.findById(dto.getProviderServiceId()).orElseThrow(() -> new EntityNotFoundException("ProviderService not found")));
        booking.setServiceAddress(addressRepository.findById(dto.getServiceAddressId()).orElseThrow(() -> new EntityNotFoundException("Address not found")));
        booking.setStatus(BookingStatusEnum.PENDING);
        return modelMapper.map(bookingRepository.save(booking), BookingResponseDTO.class);
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return modelMapper.map(booking, BookingResponseDTO.class);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> modelMapper.map(b, BookingResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO dto) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        modelMapper.map(dto, booking);
        return modelMapper.map(bookingRepository.save(booking), BookingResponseDTO.class);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteBooking(Long id) { // Soft delete
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        booking.setIsDeleted(true);
        bookingRepository.save(booking);
    }
}