package com.servesync.service.review;

import com.servesync.dto.review.ReviewRequestDTO;
import com.servesync.dto.review.ReviewResponseDTO;

//ReviewService.java
import com.servesync.dto.review.*;
import java.util.List;

public interface ReviewService {
 ReviewResponseDTO createReview(ReviewRequestDTO dto);
 ReviewResponseDTO updateReview(ReviewUpdateDTO dto);
 List<ReviewResponseDTO> getReviewsForUser(Long userId); // both customer or provider
 List<ReviewResponseDTO> getReviewsForOrder(Long orderId);
}
