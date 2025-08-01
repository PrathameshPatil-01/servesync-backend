package com.servesync.dto.role;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RoleDTO {
    private Short id;

    @NotNull(message = "Role name is required.")
    private String roleName;

    private String description;
}
