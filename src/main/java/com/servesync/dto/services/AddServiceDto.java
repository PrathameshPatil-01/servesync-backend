package com.servesync.dto.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddServiceDto {

    private String serviceName;
    private String description;
    private Boolean isActive;

    // For uploading the image
    private MultipartFile imageFile;
}
