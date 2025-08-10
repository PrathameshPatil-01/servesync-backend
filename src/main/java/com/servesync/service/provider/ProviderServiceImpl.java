package com.servesync.service.provider;

import com.servesync.dto.provider.ProviderDashboardStatsDTO;
import com.servesync.dto.provider.ProviderRequestDTO;
import com.servesync.dto.provider.ProviderResponseDTO;
import com.servesync.dto.provider.ProviderUpdateDTO;
import com.servesync.entity.provider.Provider;
import com.servesync.entity.user.User;
import com.servesync.enums.OrderStatusEnum;
import com.servesync.enums.RoleName;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.provider.ProviderRepository;
import com.servesync.repository.user.UserRepository;
import com.servesync.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.modelmapper.PropertyMap; // Import PropertyMap
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository; // For dashboard stats
    private final ModelMapper modelMapper; // Injected ModelMapper
    private final SecurityUtils securityUtils;

    @Override
    public ProviderResponseDTO registerProvider(ProviderRequestDTO dto) {
        Long currentUserId = securityUtils.getCurrentUserId();
        

        if (providerRepository.existsByUserId(currentUserId)) {
            throw new ApiException("Provider profile already exists for this user.");
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + currentUserId));

        Provider provider = modelMapper.map(dto, Provider.class); // Use ModelMapper
        provider.setUser(user);

        Provider savedProvider = providerRepository.save(provider);
        log.info("Provider registered successfully for user ID: {}", currentUserId);
        return modelMapper.map(savedProvider, ProviderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public List<ProviderResponseDTO> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(provider -> modelMapper.map(provider, ProviderResponseDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponseDTO getProviderById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + id));
        return modelMapper.map(provider, ProviderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public ProviderResponseDTO updateProvider(Long id, ProviderUpdateDTO dto) {
        Provider existingProvider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + id));

        modelMapper.map(dto, existingProvider); // Use ModelMapper for update
        Provider updatedProvider = providerRepository.save(existingProvider);
        log.info("Provider ID {} updated.", id);
        return modelMapper.map(updatedProvider, ProviderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public void deleteProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + id));
        provider.setIsDeleted(true); // Soft delete
        providerRepository.save(provider);
        log.info("Provider ID {} soft-deleted.", id);
    }

    @Override
    public ProviderResponseDTO getCurrentProviderProfile() {
        Long currentUserId = securityUtils.getCurrentUserId();
        Provider provider = providerRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found for current user."));
        return modelMapper.map(provider, ProviderResponseDTO.class); // Use ModelMapper
    }

    @Override
    public ProviderDashboardStatsDTO getProviderDashboardStats(Long providerId) {
        // Total Orders
        Long totalOrders = orderRepository.countByProviderIdAndStatus(providerId, null); // Count all orders
        // Pending Orders
        Long pendingOrders = orderRepository.countByProviderIdAndStatus(providerId, OrderStatusEnum.PENDING);
        // Completed Orders
        Long completedOrders = orderRepository.countByProviderIdAndStatus(providerId, OrderStatusEnum.COMPLETED);
        // Upcoming Appointments (e.g., confirmed or in-progress orders in the future)
        Long upcomingAppointments = (long) orderRepository.findUpcomingOrdersForProvider(providerId, LocalDateTime.now()).size();

        // Earnings (simplified, would integrate with EarningService for actual calculations)
        BigDecimal totalEarnings = orderRepository.findByProviderIdAndStatus(providerId, OrderStatusEnum.COMPLETED, Pageable.unpaged())
                .stream()
                .map(order -> order.getTotalPrice().subtract(order.getTravelFee()).subtract(order.getCancellationFee())) // Example calculation
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // For today, week, month earnings, you'd need more sophisticated queries or an EarningService
        BigDecimal todayEarnings = BigDecimal.ZERO; // Placeholder
        BigDecimal thisWeekEarnings = BigDecimal.ZERO; // Placeholder
        BigDecimal thisMonthEarnings = BigDecimal.ZERO; // Placeholder

        return ProviderDashboardStatsDTO.builder()
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrders)
                .upcomingAppointments(upcomingAppointments)
                .totalEarnings(totalEarnings)
                .todayEarnings(todayEarnings)
                .thisWeekEarnings(thisWeekEarnings)
                .thisMonthEarnings(thisMonthEarnings)
                .build();
    }

    // Security Helper Methods
    @Override
    public boolean isProviderRegisteredForCurrentUser() {
        Long currentUserId = securityUtils.getCurrentUserId();
        return providerRepository.existsByUserId(currentUserId);
    }

    @Override
    public boolean isProviderAccessible(Long providerId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        // Admin can access any provider
        if (currentUser.getRoleNames().contains(RoleName.ROLE_ADMIN.name())) {
            return true;
        }
        // Provider can access their own profile
        if (currentUser.getRoleNames().contains(RoleName.ROLE_PROVIDER.name())) {
            try {
                Long currentProviderId = securityUtils.getCurrentProviderId();
                return currentProviderId.equals(providerId);
            } catch (ResourceNotFoundException e) {
                return false; // User is provider role but no provider profile found
            }
        }
        // Customers can access any provider profile (for booking)
        if (currentUser.getRoleNames().contains(RoleName.ROLE_CUSTOMER.name())) {
            return true;
        }
        return false;
    }
}
