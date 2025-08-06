// ReviewResponseDTO.java
package com.servesync.dto.review;

import com.servesync.dto.base.BaseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO extends BaseDTO {
    private Long id;
    private Long bookingId;
    private Long reviewerId;
    private Long revieweeId;
    private int providerRating;
    private int customerRating;
    private String reviewText;
    private String response;
}

