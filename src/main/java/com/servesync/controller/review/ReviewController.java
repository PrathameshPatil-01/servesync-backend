package com.servesync.controller.review;

import com.servesync.dto.review.ReviewRequestDTO;
import com.servesync.dto.review.ReviewResponseDTO;
import com.servesync.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.addReview(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(reviewService.getReviewsByBooking(bookingId));
    }

    @GetMapping("/reviewee/{revieweeId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByReviewee(@PathVariable Long revieweeId) {
        return ResponseEntity.ok(reviewService.getReviewsByReviewee(revieweeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
