package com.servesync.dto.review;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateDTO {
 @NotNull
 private Long reviewId;

 @Min(1)
 @Max(5)
 private int rating;

 private String reviewText;

 private String response; // optional – provider can respond
}
