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

	// ---------------------- Core CRUD Operations ----------------------
	@Query("""
		    SELECT o FROM Order o
		    WHERE o.providerServiceOffer.provider.id = :providerId
		      AND o.isDeleted = FALSE
		""")
		Page<Order> findByProviderId(@Param("providerId") Long providerId, Pageable pageable);


	// ---------------------- Core Retrieval ----------------------
	@Query("""
			    SELECT o FROM Order o
			    WHERE o.providerServiceOffer.provider.id = :providerId
			      AND (:status IS NULL OR o.status = :status)
			      AND o.isDeleted = FALSE
			""")
	Page<Order> findByProviderIdAndStatus(@Param("providerId") Long providerId, @Param("status") OrderStatusEnum status,
			Pageable pageable);

	@Query("""
			    SELECT o FROM Order o
			    WHERE o.customer.id = :customerId
			      AND (:status IS NULL OR o.status = :status)
			      AND o.isDeleted = FALSE
			""")
	Page<Order> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") OrderStatusEnum status,
			Pageable pageable);

	// ---------------------- Search & Filtering ----------------------
	@Query("""
			    SELECT o FROM Order o
			    WHERE (
			        LOWER(o.customer.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			        OR LOWER(o.customer.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    )
			    AND (:status IS NULL OR o.status = :status)
			    AND o.isDeleted = FALSE
			""")
	Page<Order> searchOrders(@Param("keyword") String keyword, @Param("status") OrderStatusEnum status,
			Pageable pageable);

	@Query("""
			    SELECT o FROM Order o
			    WHERE (:status IS NULL OR o.status = :status)
			    AND o.isDeleted = FALSE
			""")
	Page<Order> findAllByStatus(@Param("status") OrderStatusEnum status, Pageable pageable);

	// ---------------------- Recent Orders ----------------------
	@Query("""
			    SELECT o FROM Order o
			    WHERE o.customer.id = :userId
			      AND o.isDeleted = FALSE
			    ORDER BY o.createdAt DESC
			""")
	List<Order> findRecentOrdersForUser(@Param("userId") Long userId, Pageable pageable);

	// ---------------------- Counts & Schedules ----------------------
	@Query("""
			    SELECT COUNT(o) FROM Order o
			    WHERE o.providerServiceOffer.provider.id = :providerId
			      AND o.status = :status
			      AND o.isDeleted = FALSE
			""")
	Long countByProviderIdAndStatus(@Param("providerId") Long providerId, @Param("status") OrderStatusEnum status);

	@Query("""
			    SELECT o FROM Order o
			    WHERE o.providerServiceOffer.provider.id = :providerId
			      AND o.scheduledStart > :now
			      AND o.status IN ('CONFIRMED', 'IN_PROGRESS')
			      AND o.isDeleted = FALSE
			    ORDER BY o.scheduledStart ASC
			""")
	List<Order> findUpcomingOrdersForProvider(@Param("providerId") Long providerId, @Param("now") LocalDateTime now);

	@Query("""
			    SELECT o FROM Order o
			    WHERE o.providerServiceOffer.provider.id = :providerId
			      AND o.scheduledStart BETWEEN :startOfDay AND :endOfDay
			      AND o.status IN ('CONFIRMED', 'IN_PROGRESS', 'COMPLETED')
			      AND o.isDeleted = FALSE
			""")
	List<Order> findProviderOrdersForDate(@Param("providerId") Long providerId,
			@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

	// ---------------------- Security Helpers ----------------------
	@Query("""
			    SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END
			    FROM Order o
			    WHERE o.id = :orderId
			      AND o.customer.id = :userId
			      AND o.isDeleted = FALSE
			""")
	boolean existsByIdAndCustomerId(@Param("orderId") Long orderId, @Param("userId") Long userId);

	@Query("""
			    SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END
			    FROM Order o
			    WHERE o.id = :orderId
			      AND o.providerServiceOffer.provider.id = :providerId
			      AND o.isDeleted = FALSE
			""")
	boolean existsByIdAndProviderId(@Param("orderId") Long orderId, @Param("providerId") Long providerId);


}
