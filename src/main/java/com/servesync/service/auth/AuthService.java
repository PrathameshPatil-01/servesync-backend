package com.servesync.service.auth;

import com.servesync.dto.auth.AuthResponseDTO;
import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.user.UserResponseDTO;
import com.servesync.entity.user.User;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO dto);
    AuthResponseDTO login(LoginRequestDTO dto);
}

