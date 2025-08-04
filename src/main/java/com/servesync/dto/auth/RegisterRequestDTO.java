package com.servesync.dto.auth;

import com.servesync.enums.RoleName;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private RoleName role;
}

