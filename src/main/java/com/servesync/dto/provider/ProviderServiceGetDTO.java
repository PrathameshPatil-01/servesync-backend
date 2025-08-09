package com.servesync.dto.provider;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderServiceGetDTO {

    private Long id;

    private Long subServiceId;

    private String subServiceName;  // if you want to include subservice details

    private Double price;

    private String description;

    // Add other fields as needed

}
