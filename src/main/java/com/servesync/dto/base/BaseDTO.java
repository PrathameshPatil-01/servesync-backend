package com.servesync.dto.base;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

// DTO base class with common audit fields
@Data
public abstract class BaseDTO {

	@JsonProperty(access =Access.READ_ONLY)
    private Boolean isDeleted;
	
	@JsonProperty(access =Access.READ_ONLY)
    private LocalDateTime createdAt;
	
	@JsonProperty(access =Access.READ_ONLY)
    private LocalDateTime updatedAt;

    // Add version field to support optimistic locking in UI if needed
    private Integer version;
}
