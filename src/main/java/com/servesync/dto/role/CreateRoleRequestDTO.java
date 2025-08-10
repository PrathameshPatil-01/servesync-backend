package com.servesync.dto.role;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateRoleRequestDTO {

    @NotNull(message = "Role name must not be null")
    private String roleName;

    @Size(max = 255, message = "Description should not exceed 255 characters")
    private String description;
}
