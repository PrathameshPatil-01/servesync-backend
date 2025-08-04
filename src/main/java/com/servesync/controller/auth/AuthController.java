package com.servesync.controller.auth;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.servesync.dto.auth.LoginRequestDTO;
import com.servesync.dto.auth.LoginResponseDTO;
import com.servesync.dto.auth.RegisterRequestDTO;
import com.servesync.dto.auth.RegisterResponseDTO;
import com.servesync.service.auth.AuthService;

import lombok.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}


