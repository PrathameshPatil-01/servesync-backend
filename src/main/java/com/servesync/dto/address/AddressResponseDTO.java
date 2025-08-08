package com.servesync.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private Long id;
    private String houseNo;

	  
    private String area;

    private String city;
 
    private String state;
 
    private String landmark;

    private String postalCode;
}