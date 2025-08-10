package com.servesync.service.order;

import com.servesync.dto.order.OrderRequestDTO;
import com.servesync.dto.order.OrderResponseDTO;
import com.servesync.entity.address.Address;
import com.servesync.entity.order.Order;
import com.servesync.entity.provider.ProviderServiceOffer;
import com.servesync.entity.user.User;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.RoleName;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.address.AddressRepository;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.provider.ProviderServiceOfferRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.modelmapper.PropertyMap; // Import PropertyMap
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl; // Import PageImpl
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProviderServiceOfferRepository providerServiceOfferRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper; // Injected ModelMapper
    private final SecurityUtils securityUtils;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        User customer = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + dto.getUserId()));

        ProviderServiceOffer providerServiceOffer = providerServiceOfferRepository.findById(dto.getProviderServiceOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service Offer not found with ID: " + dto.getProviderServiceOfferId()));

        Address serviceAddress = addressRepository.findById(dto.getServiceAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Address not found with ID: " + dto.getServiceAddressId()));

        // Basic validation: ensure address belongs to customer
        if (!serviceAddress.getUser().getId().equals(customer.getId())) {
            throw new ApiException("Service address does not belong to the specified customer.");
        }

        Order order = modelMapper.map(dto, Order.class); // Use ModelMapper
        order.setCustomer(customer);
        order.setProviderServiceOffer(providerServiceOffer);
        order.setServiceAddress(serviceAddress);
        order.setStatus(OrderStatusEnum.PENDING); // Initial status

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());
        return modelMapper.map(savedOrder, OrderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return modelMapper.map(order, OrderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        // Only allow updates if order is PENDING or CONFIRMED
        if (existingOrder.getStatus() != OrderStatusEnum.PENDING && existingOrder.getStatus() != OrderStatusEnum.CONFIRMED) {
            throw new ApiException("Cannot update order in current status: " + existingOrder.getStatus());
        }

        modelMapper.map(dto, existingOrder); // Use ModelMapper for update

        // Re-fetch related entities if IDs changed (though typically not allowed for existing orders)
        if (!existingOrder.getCustomer().getId().equals(dto.getUserId())) {
            User newCustomer = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("New customer not found with ID: " + dto.getUserId()));
            existingOrder.setCustomer(newCustomer);
        }
        if (!existingOrder.getProviderServiceOffer().getId().equals(dto.getProviderServiceOfferId())) {
            ProviderServiceOffer newProviderServiceOffer = providerServiceOfferRepository.findById(dto.getProviderServiceOfferId())
                    .orElseThrow(() -> new ResourceNotFoundException("New Provider Service Offer not found with ID: " + dto.getProviderServiceOfferId()));
            existingOrder.setProviderServiceOffer(newProviderServiceOffer);
        }
        if (!existingOrder.getServiceAddress().getId().equals(dto.getServiceAddressId())) {
            Address newServiceAddress = addressRepository.findById(dto.getServiceAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("New Service Address not found with ID: " + dto.getServiceAddressId()));
            existingOrder.setServiceAddress(newServiceAddress);
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Order updated with ID: {}", updatedOrder.getId());
        return modelMapper.map(updatedOrder, OrderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        order.setIsDeleted(true); // Soft delete
        orderRepository.save(order);
        log.info("Order soft-deleted with ID: {}", id);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusEnum newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // State machine for status transitions
        switch (order.getStatus()) {
            case PENDING:
                if (newStatus == OrderStatusEnum.CONFIRMED || newStatus == OrderStatusEnum.REJECTED || newStatus == OrderStatusEnum.CANCELLED) {
                    order.setStatus(newStatus);
                } else {
                    throw new ApiException("Invalid status transition from PENDING to " + newStatus);
                }
                break;
            case CONFIRMED:
                if (newStatus == OrderStatusEnum.IN_PROGRESS || newStatus == OrderStatusEnum.CANCELLED) {
                    order.setStatus(newStatus);
                    if (newStatus == OrderStatusEnum.IN_PROGRESS) {
                        order.setActualStart(LocalDateTime.now());
                    }
                } else {
                    throw new ApiException("Invalid status transition from CONFIRMED to " + newStatus);
                }
                break;
            case IN_PROGRESS:
                if (newStatus == OrderStatusEnum.COMPLETED || newStatus == OrderStatusEnum.CANCELLED) {
                    order.setStatus(newStatus);
                    if (newStatus == OrderStatusEnum.COMPLETED) {
                        order.setActualEnd(LocalDateTime.now());
                        // TODO: Trigger earning creation for provider
                    }
                } else {
                    throw new ApiException("Invalid status transition from IN_PROGRESS to " + newStatus);
                }
                break;
            case COMPLETED:
            case CANCELLED:
            case REJECTED:
                throw new ApiException("Cannot change status of a " + order.getStatus() + " order.");
            default:
                throw new ApiException("Unknown order status.");
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order ID {} status updated to {}", orderId, newStatus);
        return modelMapper.map(updatedOrder, OrderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public Page<OrderResponseDTO> getOrdersByProvider(Long providerId, OrderStatusEnum status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByProviderIdAndStatus(providerId, status, pageable);
        List<OrderResponseDTO> dtoList = orders.getContent().stream()
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, orders.getTotalElements());
    }

    @Override
    public Page<OrderResponseDTO> getOrdersByCustomer(Long customerId, OrderStatusEnum status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByCustomerIdAndStatus(customerId, status, pageable);
        List<OrderResponseDTO> dtoList = orders.getContent().stream()
                .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, orders.getTotalElements());
    }

    // Security Helper Methods
    @Override
    public boolean isOrderAccessible(Long orderId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Admin can access any order
        if (currentUser.getRoleNames().contains(RoleName.ROLE_ADMIN.name())) {
            return true;
        }
        // Customer can access their own orders
        if (currentUser.getRoleNames().contains(RoleName.ROLE_CUSTOMER.name()) && order.getCustomer().getId().equals(currentUserId)) {
            return true;
        }
        // Provider can access orders assigned to them
        if (currentUser.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
            try {
                Long currentProviderId = securityUtils.getCurrentProviderId();
                return order.getProviderServiceOffer().getProvider().getId().equals(currentProviderId);
            } catch (ResourceNotFoundException e) {
                // User is a provider role but no provider profile found, deny access
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isOrderOwnedByCurrentUser(Long orderId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        return orderRepository.existsByIdAndCustomerId(orderId, currentUserId);
    }

    @Override
    public boolean canUpdateOrderStatus(Long orderId, OrderStatusEnum newStatus) {
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Admin can update any status
        if (currentUser.getRoleNames().contains(RoleName.ROLE_ADMIN.name())) {
            return true;
        }

        // Customer can only cancel their own PENDING/CONFIRMED orders
        if (currentUser.getRoleNames().contains(RoleName.ROLE_CUSTOMER.name())) {
            if (order.getCustomer().getId().equals(currentUserId) && newStatus == OrderStatusEnum.CANCELLED) {
                return order.getStatus() == OrderStatusEnum.PENDING || order.getStatus() == OrderStatusEnum.CONFIRMED;
            }
            return false;
        }

        // Provider can update status of their assigned orders
        if (currentUser.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
            try {
                Long currentProviderId = securityUtils.getCurrentProviderId();
                if (!order.getProviderServiceOffer().getProvider().getId().equals(currentProviderId)) {
                    return false; // Not their order
                }

                // Provider specific transitions
                switch (order.getStatus()) {
                    case PENDING:
                        return newStatus == OrderStatusEnum.CONFIRMED || newStatus == OrderStatusEnum.REJECTED;
                    case CONFIRMED:
                        return newStatus == OrderStatusEnum.IN_PROGRESS || newStatus == OrderStatusEnum.CANCELLED;
                    case IN_PROGRESS:
                        return newStatus == OrderStatusEnum.COMPLETED || newStatus == OrderStatusEnum.CANCELLED;
                    default:
                        return false; // Cannot change status of COMPLETED, CANCELLED, REJECTED orders
                }
            } catch (ResourceNotFoundException e) {
                return false; // User is provider role but no provider profile found
            }
        }
        return false;
    }
}
