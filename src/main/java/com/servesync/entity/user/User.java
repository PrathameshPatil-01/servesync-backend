package com.servesync.entity.user;

import java.util.*;

import com.servesync.entity.base.BaseEntity;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_country_code", nullable = false, length = 5)
    private String phoneCountryCode = "+91";

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false, length = 97)
    private String password;

    @Column(name = "profile_pic", length = 500)
    private String profilePic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();
}

