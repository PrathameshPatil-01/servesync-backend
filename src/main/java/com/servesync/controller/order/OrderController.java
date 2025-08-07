package com.servesync.controller.order;

import com.servesync.dto.order.OrderRequestDTO;
import com.servesync.dto.order.OrderResponseDTO;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing service orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROVIDER', 'ADMIN') and @orderService.isOrderAccessible(#orderId)")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable @Min(1) Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an order (customer only)")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#orderId)")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable @Min(1) Long orderId, @Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, dto));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order (customer only)")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#orderId)")
    public ResponseEntity<Void> deleteOrder(@PathVariable @Min(1) Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status (provider or customer)")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROVIDER') and @orderService.canUpdateOrderStatus(#orderId, #status)")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable @Min(1) Long orderId,
            @RequestParam OrderStatusEnum status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // Provider-specific endpoints
    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get all orders for a specific provider")
    @PreAuthorize("hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId or hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByProvider(
            @PathVariable @Min(1) Long providerId,
            @RequestParam(required = false) OrderStatusEnum status,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByProvider(providerId, status, pageable));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all orders for a specific customer")
    @PreAuthorize("hasRole('CUSTOMER') and @securityUtils.getCurrentUserId() == #customerId or hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByCustomer(
            @PathVariable @Min(1) Long customerId,
            @RequestParam(required = false) OrderStatusEnum status,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId, status, pageable));
    }
}
