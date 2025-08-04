package com.servesync.dto.auth;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.role.RoleDTO;

@Data
public class RegisterResponseDTO extends BaseDTO {
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String profilePic;
	private Set<RoleDTO> roles;
}
