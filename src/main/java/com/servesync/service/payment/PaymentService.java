package com.servesync.service.payment;

public interface PaymentService {
    PaymentResponse getPaymentById(Long id);
    PaymentResponse createPayment(PaymentRequest dto);
    void updatePaymentStatus(Long id, String status);
    PaymentResponse getPaymentByBooking(Long bookingId);
}
