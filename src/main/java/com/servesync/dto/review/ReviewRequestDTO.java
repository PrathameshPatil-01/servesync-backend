package com.servesync.dto.review;


import com.servesync.enums.ReviewTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private ReviewTypeEnum reviewType;
    private int rating;
    private String comment;
}

