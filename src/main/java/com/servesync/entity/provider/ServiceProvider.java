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

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_reg_number")
    private String businessRegNumber;

    @Column(name = "business_tax_id")
    private String businessTaxId;

    private String description;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience = 0;

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