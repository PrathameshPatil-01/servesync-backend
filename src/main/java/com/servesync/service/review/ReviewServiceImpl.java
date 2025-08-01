package com.servesync.service.review;

import com.servesync.dto.review.ReviewRequestDTO;
import com.servesync.dto.review.ReviewResponseDTO;
import com.servesync.entity.booking.Booking;
//import com.servesync.entity.booking.Booking;
import com.servesync.entity.review.Review;
import com.servesync.entity.user.User;
import com.servesync.repository.booking.BookingRepository;
//import com.servesync.entity.user.User;
//import com.servesync.repository.booking.BookingRepository;
import com.servesync.repository.review.ReviewRepository;
import com.servesync.repository.user.UserRepository;

//import com.servesync.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewResponseDTO addReview(ReviewRequestDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new EntityNotFoundException("Reviewer not found"));

        User reviewee = userRepository.findById(dto.getRevieweeId())
                .orElseThrow(() -> new EntityNotFoundException("Reviewee not found"));

        Review review = modelMapper.map(dto, Review.class);
        review.setBooking(booking);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);

        return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
    }

    @Override
    public ReviewResponseDTO getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByBooking(Long bookingId) {
        return reviewRepository.findByBookingId(bookingId)
                .stream().map(r -> modelMapper.map(r, ReviewResponseDTO.class)).toList();
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByReviewee(Long revieweeId) {
        return reviewRepository.findByReviewerId(revieweeId)
                .stream().map(r -> modelMapper.map(r, ReviewResponseDTO.class)).toList();
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return modelMapper.map(reviewRepository.save(review), ReviewResponseDTO.class);
    }

    @Override
    public String deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setDeleted(true);
        reviewRepository.save(review);
        return "Review soft deleted successfully!";
    }
}
