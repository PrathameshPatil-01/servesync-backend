package com.servesync.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import com.servesync.enums.RoleName;

@Entity
@Table(name = "role_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_type_id")
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true, length = 20)
    private RoleName roleName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "roleType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();
}
