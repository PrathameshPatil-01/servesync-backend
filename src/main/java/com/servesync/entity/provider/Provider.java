package com.servesync.entity.provider;

import com.servesync.entity.base.BaseEntityWithId;
import com.servesync.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "providers") // Renamed from service_providers
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider extends BaseEntityWithId {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "aadhar_number", unique = true, length = 12)
    private String aadharNumber;

    @Column(name = "gst_number", length = 15, unique = true)
    private String gstNumber;

    @Column(name = "pan_number", unique = true, length = 10)
    private String panNumber;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience = 0;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "available_days", length = 50)
    private String availableDays; // e.g., "Mon,Tue,Wed"

    @Column(name = "available_time_start")
    private LocalTime availableTimeStart;

    @Column(name = "available_time_end")
    private LocalTime availableTimeEnd;

    @Column(name = "jobs_completed", nullable = false)
    private Integer jobsCompleted = 0;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount = 0;

    @Column(name = "average_rating", nullable = false, precision = 2)
    private Double averageRating = 0.0;

    @Column(name = "service_area_radius_km", nullable = false)
    private Integer serviceAreaRadiusKm = 10; // Default coverage area in kilometers

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "provider_documents", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "document_url")
    private Set<String> documents = new HashSet<>(); // For URLs to PAN, Aadhaar, Certifications

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProviderServiceOffer> serviceOffers = new HashSet<>(); // Renamed from providerServices

    // Utility methods
    public void addServiceOffer(ProviderServiceOffer offer) {
        serviceOffers.add(offer);
        offer.setProvider(this);
    }

    public void removeServiceOffer(ProviderServiceOffer offer) {
        serviceOffers.remove(offer);
        offer.setProvider(null);
    }
}
