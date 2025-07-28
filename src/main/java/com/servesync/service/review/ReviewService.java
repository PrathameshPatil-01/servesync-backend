package com.servesync.service.review;

public interface ReviewService {
    List<ReviewResponse> getReviewsForBooking(Long bookingId);
    ReviewResponse addReview(Long bookingId, Long reviewerId, ReviewRequest dto);
    ReviewResponse updateReview(Long reviewId, ReviewRequest dto);
    void deleteReview(Long reviewId);
}