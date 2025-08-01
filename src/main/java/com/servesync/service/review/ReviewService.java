package com.servesync.service.review;

import com.servesync.dto.review.ReviewRequestDTO;
import com.servesync.dto.review.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO addReview(ReviewRequestDTO dto);
    ReviewResponseDTO getReviewById(Long id);
    List<ReviewResponseDTO> getReviewsByBooking(Long bookingId);
    List<ReviewResponseDTO> getReviewsByReviewee(Long revieweeId);
    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto);
    String deleteReview(Long id);
}
