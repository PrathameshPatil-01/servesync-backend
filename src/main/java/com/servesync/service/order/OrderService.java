package com.servesync.service.order;

import com.servesync.dto.order.OrderRequestDTO;
import com.servesync.dto.order.OrderResponseDTO;
import com.servesync.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO dto);
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto);
    void deleteOrder(Long id);
    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusEnum status);
    Page<OrderResponseDTO> getOrdersByProvider(Long providerId, OrderStatusEnum status, Pageable pageable);
    Page<OrderResponseDTO> getOrdersByCustomer(Long customerId, OrderStatusEnum status, Pageable pageable);

    // Security helper methods
    boolean isOrderAccessible(Long orderId);
    boolean isOrderOwnedByCurrentUser(Long orderId);
    boolean canUpdateOrderStatus(Long orderId, OrderStatusEnum newStatus);
    
    
}
