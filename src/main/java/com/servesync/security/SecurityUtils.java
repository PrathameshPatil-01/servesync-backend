package com.servesync.security;

import com.servesync.exception.ApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId() {
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
}
