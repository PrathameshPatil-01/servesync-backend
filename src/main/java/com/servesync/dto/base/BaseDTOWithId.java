package com.servesync.dto.base;

import lombok.Data;

// Extends BaseDTO by adding ID field
@Data
public abstract class BaseDTOWithId extends BaseDTO {

    private Long id;
}
