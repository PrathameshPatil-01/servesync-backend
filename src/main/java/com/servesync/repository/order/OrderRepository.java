package com.servesync.repository.order;

import com.servesync.entity.order.Order;
import com.servesync.enums.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by provider ID, optionally filtered by status
    @Query("SELECT o FROM Order o WHERE o.providerServiceOffer.provider.id = :providerId " +
            "AND (:status IS NULL OR o.status = :status) AND o.isDeleted = FALSE")
    Page<Order> findByProviderIdAndStatus(@Param("providerId") Long providerId, @Param("status") OrderStatusEnum status, Pageable pageable);

    // Find orders by customer ID, optionally filtered by status
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId " +
            "AND (:status IS NULL OR o.status = :status) AND o.isDeleted = FALSE")
    Page<Order> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") OrderStatusEnum status, Pageable pageable);

    // Count orders for a provider by status
    @Query("SELECT COUNT(o) FROM Order o WHERE o.providerServiceOffer.provider.id = :providerId AND o.status = :status AND o.isDeleted = FALSE")
    Long countByProviderIdAndStatus(@Param("providerId") Long providerId, @Param("status") OrderStatusEnum status);

    // Find upcoming orders for a provider (e.g., for dashboard)
    @Query("SELECT o FROM Order o WHERE o.providerServiceOffer.provider.id = :providerId " +
            "AND o.scheduledStart > :now AND o.status IN ('CONFIRMED', 'IN_PROGRESS') AND o.isDeleted = FALSE ORDER BY o.scheduledStart ASC")
    List<Order> findUpcomingOrdersForProvider(@Param("providerId") Long providerId, @Param("now") LocalDateTime now);

    // Find orders for a provider within a specific date range (for schedule)
    @Query("SELECT o FROM Order o WHERE o.providerServiceOffer.provider.id = :providerId " +
            "AND o.scheduledStart BETWEEN :startOfDay AND :endOfDay AND o.isDeleted = FALSE " +
            "AND o.status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED')")
    List<Order> findProviderOrdersForDate(@Param("providerId") Long providerId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // Check if an order belongs to a specific user (customer)
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.id = :orderId AND o.customer.id = :userId AND o.isDeleted = FALSE")
    boolean existsByIdAndCustomerId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    // Check if an order belongs to a specific provider
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END FROM Order o WHERE o.id = :orderId AND o.providerServiceOffer.provider.id = :providerId AND o.isDeleted = FALSE")
    boolean existsByIdAndProviderId(@Param("orderId") Long orderId, @Param("providerId") Long providerId);
}
