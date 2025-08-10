package com.servesync.dto.address;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestDTO {

	
	    private String houseNo;

	  
	    private String area;

	    private String city;

	 
	    private String state;

	 
	    private String landmark;

	   
	    private String country = "India";


	    private String postalCode;
}
