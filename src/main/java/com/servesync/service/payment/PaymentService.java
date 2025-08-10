package com.servesync.service.payment;

import com.servesync.dto.payment.PaymentRequestDTO;
import com.servesync.dto.payment.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO getPaymentById(Long id);
    PaymentResponseDTO createPayment(PaymentRequestDTO request);
    PaymentResponseDTO updatePaymentStatus(Long id, String status);
    PaymentResponseDTO getPaymentByOrderId(Long orderId);
}