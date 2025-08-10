package com.servesync.entity.service;

import com.servesync.entity.base.BaseEntityWithId;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service extends BaseEntityWithId {

    @Column(name = "service_name", nullable = false, unique = true)
    private String serviceName;

    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubService> subServices = new HashSet<>();

    public void addSubService(SubService subService) {
        subServices.add(subService);
        subService.setService(this);
    }

    public void removeSubService(SubService subService) {
        subServices.remove(subService);
        subService.setService(null);
    }
}