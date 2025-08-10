package com.servesync.dto.auth;
import lombok.*;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
