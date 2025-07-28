package com.servesync.service.user;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse createUser(CreateUserRequest dto);
    UserResponse updateUser(Long id, UpdateUserRequest dto);
    void deleteUser(Long id);
}