// ReviewRequestDTO.java
package com.servesync.dto.review;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    @NotNull
    private Long orderId;

    @NotNull
    private Long reviewerId;

    @NotNull
    private Long revieweeId;

    @Min(1)
    @Max(5)
    private int rating;

    private String reviewText;
}
