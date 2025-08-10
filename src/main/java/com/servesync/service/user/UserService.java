package com.servesync.service.user;

import com.servesync.dto.user.UserCreateDTO;
import com.servesync.dto.user.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO dto);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    void deleteUser(Long id);
}