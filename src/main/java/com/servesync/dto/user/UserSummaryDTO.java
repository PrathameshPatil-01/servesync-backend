package com.servesync.dto.user;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserSummaryDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profilePic;
    private Set<String> roles; // or List<String>
}
