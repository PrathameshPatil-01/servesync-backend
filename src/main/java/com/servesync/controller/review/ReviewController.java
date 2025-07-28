package com.servesync.controller.review;

import com.servesync.dto.user.*;
import com.servesync.dto.address.*;
import com.servesync.dto.booking.*;
import com.servesync.dto.payment.*;
import com.servesync.dto.review.*;
import com.servesync.dto.provider.*;
import com.servesync.dto.verification.*;
import com.servesync.dto.audit.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get reviews for a booking")
    public ResponseEntity<List<ReviewResponse>> getReviewsForBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/booking/{bookingId}/reviewer/{reviewerId}")
    @Operation(summary = "Add review for booking")
    public ResponseEntity<ReviewResponse> addReview(@PathVariable Long bookingId, @PathVariable Long reviewerId, @RequestBody @Valid ReviewRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewResponse());
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update review")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long reviewId, @RequestBody @Valid ReviewRequest dto) {
        return ResponseEntity.ok(new ReviewResponse());
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete review")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok("Review deleted");
    }
}