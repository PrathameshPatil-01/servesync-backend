package com.servesync.service.booking;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse getBookingById(Long id);
    BookingResponse createBooking(BookingRequest dto);
    void updateBookingStatus(Long id, String status);
    void cancelBooking(Long id);
}
