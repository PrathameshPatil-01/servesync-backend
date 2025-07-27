package com.servesync.dto.auth;

import lombok.Data;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class AuthResponseDTO {
    private String jwt;
    private Long userId;
    private String email;
    private List<String> authorities;
}

