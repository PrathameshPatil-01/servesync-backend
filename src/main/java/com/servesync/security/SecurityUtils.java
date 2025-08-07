package com.servesync.security;

import com.servesync.entity.provider.Provider;
import com.servesync.exception.ApiException;
import com.servesync.exception.ResourceNotFoundException;
import com.servesync.repository.provider.ProviderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component // Make it a Spring component to be injectable
public class SecurityUtils {

    private final ProviderRepository providerRepository;

    public SecurityUtils(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new ApiException("Unauthenticated");
        }
        if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new ApiException("Invalid authentication principal");
        }
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
        return details.getId();
    }

    public Long getCurrentProviderId() {
        Long userId = getCurrentUserId();
        Provider provider = providerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found for current user."));
        return provider.getId();
    }
}
