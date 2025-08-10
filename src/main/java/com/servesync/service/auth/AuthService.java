package com.servesync.service.auth;

import com.servesync.dto.auth.LoginResponseDTO;
import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.auth.RegisterResponseDTO;

public interface AuthService {
	RegisterResponseDTO register(RegisterRequestDTO dto);
    LoginResponseDTO login(LoginRequestDTO dto);
}

