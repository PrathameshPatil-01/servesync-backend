package com.servesync.service.payment;

import com.servesync.dto.payment.PaymentRequestDTO;
import com.servesync.dto.payment.PaymentResponseDTO;
import com.servesync.entity.order.Order;
import com.servesync.entity.payment.Payment;
import com.servesync.enums.PaymentStatusEnum;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.payment.PaymentRepository;
import com.servesync.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return modelMapper.map(payment, PaymentResponseDTO.class); // Use ModelMapper
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        Payment payment = modelMapper.map(request, Payment.class); // Use ModelMapper
        payment.setOrder(order);
        payment.setStatus(PaymentStatusEnum.PENDING);
        payment.setPaidAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentResponseDTO.class); // Use ModelMapper
    }

    @Override
    public PaymentResponseDTO updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        payment.setStatus(PaymentStatusEnum.valueOf(status.toUpperCase()));
        return modelMapper.map(paymentRepository.save(payment), PaymentResponseDTO.class); // Use ModelMapper
    }

    @Override
    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for Order ID: " + orderId));
        return modelMapper.map(payment, PaymentResponseDTO.class); // Use ModelMapper
    }
}
