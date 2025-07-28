package com.servesync.entity.review;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.booking.Booking;
import com.servesync.entity.user.User;
import com.servesync.enums.ReviewTypeEnum;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"booking_id", "reviewer_id", "review_type"}),
                            @UniqueConstraint(columnNames = {"booking_id", "reviewee_id", "review_type"})},
       indexes = {
           @Index(name = "idx_reviewee", columnList = "reviewee_id, review_type"),
           @Index(name = "idx_booking_review", columnList = "booking_id, review_type")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntityWithId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

	@Enumerated(EnumType.STRING)
	@Column(name = "review_type", nullable = false)
    private ReviewTypeEnum reviewType;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}