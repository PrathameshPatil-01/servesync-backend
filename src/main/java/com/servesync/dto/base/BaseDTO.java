package com.servesync.dto.base;

import lombok.Data;

import java.time.LocalDateTime;

// DTO base class with common audit fields
@Data
public abstract class BaseDTO {

    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Add version field to support optimistic locking in UI if needed
    private Integer version;
}
