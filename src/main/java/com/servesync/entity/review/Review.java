package com.servesync.entity.review;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.booking.Booking;
import com.servesync.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "reviews",
    uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "reviewer_id", "reviewee_id"}),
    indexes = {
        @Index(name = "idx_reviewee", columnList = "reviewee_id"),
        @Index(name = "idx_booking_review", columnList = "booking_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @Column(name = "provider_rating", columnDefinition = "TINYINT CHECK (provider_rating BETWEEN 1 AND 5)")
    private Integer providerRating;

    @Column(name = "customer_rating", columnDefinition = "TINYINT CHECK (customer_rating BETWEEN 1 AND 5)")
    private Integer customerRating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;
}
