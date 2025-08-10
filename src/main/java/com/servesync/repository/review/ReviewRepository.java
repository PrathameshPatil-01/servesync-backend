package com.servesync.repository.review;
import com.servesync.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByRevieweeIdAndIsDeletedFalse(Long userId);

	// Corrected method signature to return List<Review> for a order ID
	List<Review> findByOrderIdAndIsDeletedFalse(Long orderId);
}

