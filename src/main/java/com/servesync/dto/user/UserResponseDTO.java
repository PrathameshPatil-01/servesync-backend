package com.servesync.dto.user;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import com.servesync.dto.base.BaseDTO;
import com.servesync.dto.role.RoleDTO;

@Data
public class UserResponseDTO extends BaseDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String profilePic;
	private Set<RoleDTO> roles;
}
