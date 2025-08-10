package com.servesync.controller.payment;

import com.servesync.dto.payment.PaymentRequestDTO;
import com.servesync.dto.payment.PaymentResponseDTO;
import com.servesync.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PostMapping
    @Operation(summary = "Initiate payment")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody @Valid PaymentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(@PathVariable @Min(1) Long id,
                                                               @RequestParam String status) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, status));
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get payment by booking ID")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrder(@PathVariable @Min(1) Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(bookingId));
    }
}