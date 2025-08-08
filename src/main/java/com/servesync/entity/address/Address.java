package com.servesync.entity.address;

import java.math.BigDecimal;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntityWithId {

    @Column(name = "house_no", nullable = false)
    private String houseNo;

    @Column(name = "area")
    private String area;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String landmark;

    @Column(nullable = true)
    private String country = "India";


    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
