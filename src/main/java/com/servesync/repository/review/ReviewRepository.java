package com.servesync.repository.review;
import com.servesync.entity.review.Review;
import com.servesync.enums.ReviewTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookingId(Long bookingId);

    List<Review> findByRevieweeIdAndReviewType(Long revieweeId, ReviewTypeEnum reviewType);

    List<Review> findByReviewerId(Long reviewerId);
}
