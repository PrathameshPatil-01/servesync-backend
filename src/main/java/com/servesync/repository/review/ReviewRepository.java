package com.servesync.repository.review;
import com.servesync.entity.booking.Booking;
import com.servesync.entity.review.Review;
import com.servesync.enums.ReviewTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByRevieweeIdAndIsDeletedFalse(Long userId);

	Optional<Booking> findByBookingIdAndIsDeletedFalse(Long bookingId);
}
