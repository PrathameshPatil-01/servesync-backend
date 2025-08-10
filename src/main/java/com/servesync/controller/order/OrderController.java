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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing service orders between customers and providers")
public class OrderController {

    private final OrderService orderService;

    // ---------------------------
    // CREATE
    // ---------------------------
    @PostMapping
    @Operation(summary = "Create a new order (Customer only)")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO created = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------------
    // READ
    // ---------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID (owner provider/customer or admin)")
    // Delegate actual accessibility logic to orderService.isOrderAccessible(#id)
    @PreAuthorize("@orderService.isOrderAccessible(#id)")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // ---------------------------
    // UPDATE (Customer)
    // ---------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Update order details (Customer only, typically before provider acceptance)")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#id)")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable("id") @Min(1) Long id,
            @Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.updateOrder(id, dto));
    }

    // ---------------------------
    // DELETE (Customer)
    // ---------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order (Customer only, allowed by business rules)")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#id)")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") @Min(1) Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // ---------------------------
    // STATUS MANAGEMENT (generic)
    // ---------------------------
    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status (allowed actors depending on status transition)")
    // Business rules enforced inside canUpdateOrderStatus
    @PreAuthorize("@orderService.canUpdateOrderStatus(#orderId, #status)")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable("orderId") @Min(1) Long orderId,
            @RequestParam OrderStatusEnum status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // ---------------------------
    // PROVIDER ACTIONS
    // ---------------------------
    @PostMapping("/{orderId}/accept")
    @Operation(summary = "Provider accepts the order (Provider only)")
    @PreAuthorize("(hasRole('PROVIDER') and @orderService.isOrderAccessible(#orderId)) or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> acceptOrder(@PathVariable("orderId") @Min(1) Long orderId) {
        return ResponseEntity.ok(orderService.acceptOrder(orderId));
    }

    @PostMapping("/{orderId}/reject")
    @Operation(summary = "Provider rejects the order (Provider only)")
    @PreAuthorize("(hasRole('PROVIDER') and @orderService.isOrderAccessible(#orderId)) or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> rejectOrder(
            @PathVariable("orderId") @Min(1) Long orderId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(orderService.rejectOrder(orderId, reason));
    }

    @PostMapping("/{orderId}/start")
    @Operation(summary = "Provider marks order In-Progress (Provider only)")
    @PreAuthorize("(hasRole('PROVIDER') and @orderService.isOrderAccessible(#orderId)) or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> markOrderInProgress(@PathVariable("orderId") @Min(1) Long orderId) {
        return ResponseEntity.ok(orderService.markOrderInProgress(orderId));
    }

    @PostMapping("/{orderId}/complete")
    @Operation(summary = "Provider marks order Completed (Provider only)")
    @PreAuthorize("(hasRole('PROVIDER') and @orderService.isOrderAccessible(#orderId)) or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> markOrderCompleted(@PathVariable("orderId") @Min(1) Long orderId) {
        return ResponseEntity.ok(orderService.markOrderCompleted(orderId));
    }

    // ---------------------------
    // CUSTOMER ACTIONS
    // ---------------------------
    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "Customer cancels order (Customer only) — business logic may restrict timing / fees")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#orderId) or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> cancelOrderByCustomer(
            @PathVariable("orderId") @Min(1) Long orderId,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(orderService.cancelOrderByCustomer(orderId, reason));
    }

    @PostMapping("/{orderId}/modify")
    @Operation(summary = "Customer requests modification of an existing order (Customer only)")
    @PreAuthorize("hasRole('CUSTOMER') and @orderService.isOrderOwnedByCurrentUser(#orderId)")
    public ResponseEntity<OrderResponseDTO> requestOrderModification(
            @PathVariable("orderId") @Min(1) Long orderId,
            @Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.requestOrderModification(orderId, dto));
    }

    // ---------------------------
    // ADMIN ACTIONS
    // ---------------------------
    @PostMapping("/{orderId}/reassign")
    @Operation(summary = "Admin reassigns order to another provider (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> reassignOrder(
            @PathVariable("orderId") @Min(1) Long orderId,
            @RequestParam @Min(1) Long newProviderId) {
        return ResponseEntity.ok(orderService.reassignOrder(orderId, newProviderId));
    }

    @PostMapping("/{orderId}/force-cancel")
    @Operation(summary = "Admin force-cancels an order (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> forceCancelOrder(
            @PathVariable("orderId") @Min(1) Long orderId,
            @RequestParam(required = false) String reason) {
        orderService.forceCancelOrder(orderId, reason);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------
    // RETRIEVAL / LISTS
    // ---------------------------
    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get orders for a provider (Provider can only list their own orders; Admin can list any)")
    // Use securityUtils.getCurrentProviderId() in SpEL if you have that bean; fallback to role check + service-level check
    @PreAuthorize("((hasRole('PROVIDER') and @securityUtils.getCurrentProviderId() == #providerId) or hasRole('ADMIN'))")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByProvider(
            @PathVariable("providerId") @Min(1) Long providerId,
            @RequestParam(required = false) OrderStatusEnum status,
            @PageableDefault(sort = "scheduledStart", direction = Sort.Direction.DESC) Pageable pageable) {
        // service should also validate that providerId belongs to caller when role=PROVIDER
        return ResponseEntity.ok(orderService.getOrdersByProvider(providerId, status, pageable));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders for a customer (Customer can only list their own orders; Admin can list any)")
    @PreAuthorize("((hasRole('CUSTOMER') and @securityUtils.getCurrentUserId() == #customerId) or hasRole('ADMIN'))")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByCustomer(
            @PathVariable("customerId") @Min(1) Long customerId,
            @RequestParam(required = false) OrderStatusEnum status,
            @PageableDefault(sort = "scheduledStart", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId, status, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search orders (Admin only) — keyword & optional status filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> searchOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatusEnum status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.searchOrders(keyword, status, pageable));
    }

    @GetMapping
    @Operation(summary = "Get all orders (Admin only or scoped calls depending on service implementation)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(required = false) OrderStatusEnum status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(status, pageable));
    }



}
