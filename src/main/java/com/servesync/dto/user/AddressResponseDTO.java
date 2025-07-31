package com.servesync.dto.user;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDTO {
    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private Long userId; // ✅ Shows which user owns the address
}
