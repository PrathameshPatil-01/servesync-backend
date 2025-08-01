package com.servesync.service.payment;

import com.servesync.dto.payment.PaymentRequestDTO;
import com.servesync.dto.payment.PaymentResponseDTO;
import com.servesync.entity.booking.Booking;
import com.servesync.entity.payment.Payment;
import com.servesync.enums.PaymentStatusEnum;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.booking.BookingRepository;
import com.servesync.repository.payment.PaymentRepository;
import com.servesync.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + request.getBookingId()));

        Payment payment = modelMapper.map(request, Payment.class);
        payment.setBooking(booking);
        payment.setStatus(PaymentStatusEnum.PENDING);
        payment.setPaidAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentResponseDTO.class);
    }

    @Override
    public PaymentResponseDTO updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        payment.setStatus(PaymentStatusEnum.valueOf(status.toUpperCase()));
        return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class);
    }

    @Override
    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for Booking ID: " + bookingId));
        return modelMapper.map(payment, PaymentResponseDTO.class);
    }
}