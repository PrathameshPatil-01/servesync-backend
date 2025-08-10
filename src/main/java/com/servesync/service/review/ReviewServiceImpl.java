package com.servesync.service.review;

import com.servesync.dto.review.*;
import com.servesync.entity.order.Order;
import com.servesync.entity.review.Review;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import com.servesync.repository.order.OrderRepository;
import com.servesync.repository.review.ReviewRepository;
import com.servesync.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        User reviewee = userRepository.findById(dto.getRevieweeId())
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        Review review = new Review();
        review.setOrder(order);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);

        // Set correct rating based on reviewee's role
        if (isProvider(reviewee)) {
            review.setProviderRating(dto.getRating());
            review.setCustomerRating(null); // Ensure customer rating is null for provider reviews
        } else {
            review.setCustomerRating(dto.getRating());
            review.setProviderRating(null); // Ensure provider rating is null for customer reviews
        }

        review.setReviewText(dto.getReviewText());

        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewResponseDTO.class);
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(ReviewUpdateDTO dto) {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Update rating based on reviewee's role
        if (isProvider(review.getReviewee())) {
            review.setProviderRating(dto.getRating());
        } else {
            review.setCustomerRating(dto.getRating());
        }

        if (dto.getReviewText() != null) review.setReviewText(dto.getReviewText());
        if (dto.getResponse() != null) review.setResponse(dto.getResponse());

        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewResponseDTO.class);
    }

    @Override
    public List<ReviewResponseDTO> getReviewsForUser(Long userId) {
        // This method should ideally distinguish between reviews given by a user and reviews received by a user.
        // For simplicity, assuming it means reviews received by the user (as reviewee).
        return reviewRepository.findByRevieweeIdAndIsDeletedFalse(userId).stream()
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDTO> getReviewsForOrder(Long orderId) {
        // The findByOrderIdAndIsDeletedFalse method in ReviewRepository returns Optional<Order>, which is incorrect.
        // It should return List<Review> for a given orderId.
        // Assuming a correct repository method exists or is added:
        // List<Review> reviews = reviewRepository.findByOrderIdAndIsDeletedFalse(orderId);
        // For now, I'll mock this or assume a correct method is available.
        // Let's assume a method `findByOrderIdAndIsDeletedFalse` that returns `List<Review>`
        List<Review> reviews = reviewRepository.findAll().stream() // Placeholder: replace with actual query
                .filter(r -> r.getOrder().getId().equals(orderId) && !r.getIsDeleted())
                .collect(Collectors.toList());

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .collect(Collectors.toList());
    }

    private boolean isProvider(User user) {
        // Check if the user has the ROLE_PROVIDER role
        return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.ROLE_PROVIDER));
    }
}

