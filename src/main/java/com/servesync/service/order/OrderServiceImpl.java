package com.servesync.service.order;

import com.servesync.dto.order.OrderRequestDTO;
import com.servesync.dto.order.OrderResponseDTO;
import com.servesync.entity.address.Address;
import com.servesync.entity.order.Order;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.provider.ProviderServiceOffer;
import com.servesync.entity.user.User;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.RoleName;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.address.AddressRepository;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.provider.ProviderRepository;
import com.servesync.repository.provider.ProviderServiceOfferRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.constraints.Min;
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
    private final ProviderRepository providerRepository;
    private final ProviderServiceOfferRepository providerServiceOfferRepository;
    private final AddressRepository addressRepository;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;

    // ---------------------- Core CRUD ----------------------
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        User customer = getUser(dto.getCustomerId(), "Customer");
        ProviderServiceOffer offer = getProviderServiceOffer(dto.getProviderServiceOfferId());
        Address address = getAddress(dto.getServiceAddressId());

        if (!address.getUser().getId().equals(customer.getId())) {
            throw new ApiException("Service address does not belong to the specified customer.");
        }

        Order order = modelMapper.map(dto, Order.class);
        order.setCustomer(customer);
        order.setProviderServiceOffer(offer);
        order.setServiceAddress(address);
        order.setStatus(OrderStatusEnum.PENDING);

        Order saved = orderRepository.save(order);
        log.info("Order created with ID: {}", saved.getId());
        return modelMapper.map(saved, OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        return modelMapper.map(getOrder(id), OrderResponseDTO.class);
    }

    @Override
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO dto) {
        Order existing = getOrder(id);

        if (!(existing.getStatus() == OrderStatusEnum.PENDING || existing.getStatus() == OrderStatusEnum.CONFIRMED)) {
            throw new ApiException("Cannot update order in current status: " + existing.getStatus());
        }

        modelMapper.map(dto, existing);
        existing.setCustomer(getUser(dto.getCustomerId(), "Customer"));
        existing.setProviderServiceOffer(getProviderServiceOffer(dto.getProviderServiceOfferId()));
        existing.setServiceAddress(getAddress(dto.getServiceAddressId()));

        Order updated = orderRepository.save(existing);
        log.info("Order updated with ID: {}", updated.getId());
        return modelMapper.map(updated, OrderResponseDTO.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = getOrder(id);
        order.setIsDeleted(true);
        orderRepository.save(order);
        log.info("Order soft-deleted with ID: {}", id);
    }

    // ---------------------- Status Management ----------------------
    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusEnum status) {
        Order order = getOrder(orderId);
        validateStatusTransition(order.getStatus(), status);
        applyStatusChange(order, status);
        return modelMapper.map(orderRepository.save(order), OrderResponseDTO.class);
    }

    // ---------------------- Provider Actions ----------------------
    @Override
    public OrderResponseDTO acceptOrder(@Min(1) Long orderId) {
        return updateOrderStatus(orderId, OrderStatusEnum.CONFIRMED);
    }

    @Override
    public OrderResponseDTO rejectOrder(@Min(1) Long orderId, String reason) {
        log.info("Order {} rejected with reason: {}", orderId, reason);
        return updateOrderStatus(orderId, OrderStatusEnum.REJECTED);
    }

    @Override
    public OrderResponseDTO markOrderInProgress(@Min(1) Long orderId) {
        return updateOrderStatus(orderId, OrderStatusEnum.IN_PROGRESS);
    }

    @Override
    public OrderResponseDTO markOrderCompleted(@Min(1) Long orderId) {
        return updateOrderStatus(orderId, OrderStatusEnum.COMPLETED);
    }

    // ---------------------- Customer Actions ----------------------
    @Override
    public OrderResponseDTO cancelOrderByCustomer(@Min(1) Long orderId, String reason) {
        log.info("Customer canceled order {} with reason: {}", orderId, reason);
        return updateOrderStatus(orderId, OrderStatusEnum.CANCELLED);
    }

    @Override
    public OrderResponseDTO requestOrderModification(@Min(1) Long orderId, OrderRequestDTO dto) {
        return updateOrder(orderId, dto);
    }

    // ---------------------- Admin Actions ----------------------
    @Override
    public OrderResponseDTO reassignOrder(@Min(1) Long orderId, Long newProviderId) {
        Order order = getOrder(orderId);
        Provider provider = providerRepository.findById(newProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + newProviderId));

        order.getProviderServiceOffer().setProvider(provider);
        log.info("Order {} reassigned to provider {}", orderId, newProviderId);
        return modelMapper.map(orderRepository.save(order), OrderResponseDTO.class);
    }

    @Override
    public void forceCancelOrder(@Min(1) Long orderId, String reason) {
        log.warn("Order {} force-canceled by admin. Reason: {}", orderId, reason);
        updateOrderStatus(orderId, OrderStatusEnum.CANCELLED);
    }

    // ---------------------- Retrieval Queries ----------------------
    @Override
    public Page<OrderResponseDTO> getOrdersByProvider(Long providerId, OrderStatusEnum status, Pageable pageable) {
        Page<Order> orders;
        if (status != null) {
            orders = orderRepository.findByProviderIdAndStatus(providerId, status, pageable);
        } else {
            orders = orderRepository.findByProviderId(providerId, pageable);
        }
        return toPage(orders, pageable);
    }


    @Override
    public Page<OrderResponseDTO> getOrdersByCustomer(Long customerId, OrderStatusEnum status, Pageable pageable) {
        return toPage(orderRepository.findByCustomerIdAndStatus(customerId, status, pageable), pageable);
    }

    @Override
    public Page<OrderResponseDTO> searchOrders(String keyword, OrderStatusEnum status, Pageable pageable) {
        return toPage(orderRepository.searchOrders(keyword, status, pageable), pageable);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(OrderStatusEnum status, Pageable pageable) {
        return toPage(orderRepository.findAllByStatus(status, pageable), pageable);
    }


    // ---------------------- Security Helpers ----------------------
    @Override
    public boolean isOrderAccessible(Long orderId) {
        return checkAccess(orderId);
    }

    @Override
    public boolean isOrderOwnedByCurrentUser(Long orderId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        return orderRepository.existsByIdAndCustomerId(orderId, currentUserId);
    }

    @Override
    public boolean canUpdateOrderStatus(Long orderId, OrderStatusEnum newStatus) {
        return checkStatusPermission(orderId, newStatus);
    }

    // ---------------------- Private Helpers ----------------------
    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    private User getUser(Long id, String roleName) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(roleName + " not found with ID: " + id));
    }

    private ProviderServiceOffer getProviderServiceOffer(Long id) {
        return providerServiceOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider Service Offer not found with ID: " + id));
    }

    private Address getAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Address not found with ID: " + id));
    }

    private void validateStatusTransition(OrderStatusEnum current, OrderStatusEnum target) {
        switch (current) {
            case PENDING -> {
                if (!(target == OrderStatusEnum.CONFIRMED || target == OrderStatusEnum.REJECTED || target == OrderStatusEnum.CANCELLED)) {
                    throw new ApiException("Invalid transition from PENDING to " + target);
                }
            }
            case CONFIRMED -> {
                if (target == OrderStatusEnum.IN_PROGRESS) return;
                if (target == OrderStatusEnum.CANCELLED) return;
                throw new ApiException("Invalid transition from CONFIRMED to " + target);
            }
            case IN_PROGRESS -> {
                if (target == OrderStatusEnum.COMPLETED || target == OrderStatusEnum.CANCELLED) return;
                throw new ApiException("Invalid transition from IN_PROGRESS to " + target);
            }
            default -> throw new ApiException("Order in status " + current + " cannot be changed.");
        }
    }

    private void applyStatusChange(Order order, OrderStatusEnum newStatus) {
        order.setStatus(newStatus);
        if (newStatus == OrderStatusEnum.IN_PROGRESS) order.setActualStart(LocalDateTime.now());
        if (newStatus == OrderStatusEnum.COMPLETED) order.setActualEnd(LocalDateTime.now());
    }

    private Page<OrderResponseDTO> toPage(Page<Order> orders, Pageable pageable) {
        return new PageImpl<>(
                orders.stream().map(o -> modelMapper.map(o, OrderResponseDTO.class)).collect(Collectors.toList()),
                pageable,
                orders.getTotalElements()
        );
    }

    private boolean checkAccess(Long orderId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = getUser(currentUserId, "Current user");
        Order order = getOrder(orderId);

        if (currentUser.getRoleNames().contains(RoleName.ROLE_ADMIN.name())) return true;
        if (currentUser.getRoleNames().contains(RoleName.ROLE_CUSTOMER.name()) &&
                order.getCustomer().getId().equals(currentUserId)) return true;
        if (currentUser.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
            try {
                Long currentProviderId = securityUtils.getCurrentProviderId();
                return order.getProviderServiceOffer().getProvider().getId().equals(currentProviderId);
            } catch (ResourceNotFoundException e) {
                return false;
            }
        }
        return false;
    }

    private boolean checkStatusPermission(Long orderId, OrderStatusEnum newStatus) {
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = getUser(currentUserId, "Current user");
        Order order = getOrder(orderId);

        if (currentUser.getRoleNames().contains(RoleName.ROLE_ADMIN.name())) return true;
        if (currentUser.getRoleNames().contains(RoleName.ROLE_CUSTOMER.name())) {
            return order.getCustomer().getId().equals(currentUserId) &&
                    newStatus == OrderStatusEnum.CANCELLED &&
                    (order.getStatus() == OrderStatusEnum.PENDING || order.getStatus() == OrderStatusEnum.CONFIRMED);
        }
        if (currentUser.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
            try {
                Long providerId = securityUtils.getCurrentProviderId();
                if (!order.getProviderServiceOffer().getProvider().getId().equals(providerId)) return false;
                return switch (order.getStatus()) {
                    case PENDING -> newStatus == OrderStatusEnum.CONFIRMED || newStatus == OrderStatusEnum.REJECTED;
                    case CONFIRMED -> newStatus == OrderStatusEnum.IN_PROGRESS || newStatus == OrderStatusEnum.CANCELLED;
                    case IN_PROGRESS -> newStatus == OrderStatusEnum.COMPLETED || newStatus == OrderStatusEnum.CANCELLED;
                    default -> false;
                };
            } catch (ResourceNotFoundException e) {
                return false;
            }
        }
        return false;
    }
}
