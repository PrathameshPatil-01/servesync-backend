package com.servesync.controller.review;

import com.servesync.dto.review.ReviewRequestDTO;
import com.servesync.dto.review.ReviewResponseDTO;
import com.servesync.dto.review.ReviewUpdateDTO;
import com.servesync.service.review.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Create a new review
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO createdReview = reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // Update an existing review
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateDTO dto
    ) {
        dto.setReviewId(reviewId); // ensure ID is set from path
        ReviewResponseDTO updatedReview = reviewService.updateReview(dto);
        return ResponseEntity.ok(updatedReview);
    }

    // Get all reviews for a user (customer or provider)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsForUser(@PathVariable Long userId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsForUser(userId);
        return ResponseEntity.ok(reviews);
    }

    // Get all reviews for a specific booking
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsForOrder(@PathVariable Long bookingId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsForOrder(bookingId);
        return ResponseEntity.ok(reviews);
    }

//    // Optional delete endpoint
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
//        reviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build(); // 204 No Content
//    }
}
