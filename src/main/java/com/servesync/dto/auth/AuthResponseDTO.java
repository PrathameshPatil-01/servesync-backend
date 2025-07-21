package com.servesync.dto.auth;
import lombok.*;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String role;
}

