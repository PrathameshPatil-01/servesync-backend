package com.servesync.service.booking;

import com.servesync.dto.booking.BookingRequestDTO;
import com.servesync.dto.booking.BookingResponseDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO dto);
    BookingResponseDTO getBookingById(Long id);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO dto);
    void deleteBooking(Long id);
    void softDeleteBooking(Long id);

}