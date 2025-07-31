package com.servesync.dto.review;

import com.servesync.enums.ReviewTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {
    private Long id;
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private ReviewTypeEnum reviewType;
    private int rating;
    private String comment;
    private String response;
    private boolean isDeleted;
}
