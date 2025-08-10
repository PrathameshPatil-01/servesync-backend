package com.servesync.repository.provider;

import com.servesync.entity.provider.ProviderServiceOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderServiceOfferRepository extends JpaRepository<ProviderServiceOffer, Long> {
    List<ProviderServiceOffer> findByProviderId(Long providerId);

    @Query("SELECT CASE WHEN COUNT(pso) > 0 THEN TRUE ELSE FALSE END FROM ProviderServiceOffer pso WHERE pso.id = :offerId AND pso.provider.id = :providerId AND pso.isDeleted = FALSE")
    boolean existsByIdAndProviderId(@Param("offerId") Long offerId, @Param("providerId") Long providerId);
}
