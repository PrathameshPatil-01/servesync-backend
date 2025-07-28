package com.servesync.dto.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;              // was 'jwt' before
    private Long userId;
    private String email;
    private List<String> roles;       // was 'authorities' before
}
