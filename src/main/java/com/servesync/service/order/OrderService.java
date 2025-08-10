package com.servesync.service.order;

import com.servesync.dto.order.OrderRequestDTO;
import com.servesync.dto.order.OrderResponseDTO;
import com.servesync.enums.OrderStatusEnum;

import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    // --- Core CRUD ---
    OrderResponseDTO createOrder(OrderRequestDTO dto);
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto);
    void deleteOrder(Long id);

    // --- Status management ---
    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusEnum status);

    // Provider actions
    OrderResponseDTO acceptOrder(@Min(1) Long orderId);
    OrderResponseDTO rejectOrder(@Min(1) Long orderId, String reason);
    OrderResponseDTO markOrderInProgress(@Min(1) Long orderId);
    OrderResponseDTO markOrderCompleted(@Min(1) Long orderId);

    // Customer actions
    OrderResponseDTO cancelOrderByCustomer(@Min(1) Long orderId, String reason);
    OrderResponseDTO requestOrderModification(@Min(1) Long orderId, OrderRequestDTO dto);

    // Admin actions
    OrderResponseDTO reassignOrder(@Min(1) Long orderId, Long newProviderId);
    void forceCancelOrder(@Min(1) Long orderId, String reason);

    // --- Retrieval queries ---
    Page<OrderResponseDTO> getOrdersByProvider(Long providerId, OrderStatusEnum status, Pageable pageable);
    Page<OrderResponseDTO> getOrdersByCustomer(Long customerId, OrderStatusEnum status, Pageable pageable);
    Page<OrderResponseDTO> searchOrders(String keyword, OrderStatusEnum status, Pageable pageable);
    Page<OrderResponseDTO> getAllOrders(OrderStatusEnum status, Pageable pageable);


    // --- Security helper methods ---
    boolean isOrderAccessible(Long orderId);
    boolean isOrderOwnedByCurrentUser(Long orderId);
    boolean canUpdateOrderStatus(Long orderId, OrderStatusEnum newStatus);
}
