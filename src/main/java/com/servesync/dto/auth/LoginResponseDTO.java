package com.servesync.dto.auth;

import lombok.*;

import java.util.List;
import java.util.Set;

import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.role.RoleDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO extends BaseDTO {
    private String token;
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String profilePic;
    private List<String> roles;
    private Long providerId; // Optional, only for providers
}

