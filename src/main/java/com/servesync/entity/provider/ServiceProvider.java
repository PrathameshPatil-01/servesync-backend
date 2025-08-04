package com.servesync.entity.provider;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider extends BaseEntityWithId {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "aadhar_number", unique = true)
    private String aadharNumber;

    @Column(name = "pan_number", unique = true)
    private String panNumber;

    private String skills;

    private String description;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience = 0;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProviderService> providerServices = new HashSet<>();

    public void addProviderService(ProviderService ps) {
        providerServices.add(ps);
        ps.setProvider(this);
    }

    public void removeProviderService(ProviderService ps) {
        providerServices.remove(ps);
        ps.setProvider(null);
    }
}
