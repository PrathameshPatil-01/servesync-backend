package com.servesync.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Value("${jwt.token-prefix:Bearer }")
    private String tokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String headerValue = request.getHeader("Authorization");
        
        // Log the incoming request URI and Authorization header for debugging

        if (headerValue != null && headerValue.startsWith(tokenPrefix)) {

            try {
                String jwt = headerValue.substring(tokenPrefix.length());
                Authentication authentication = jwtUtils.populateAuthenticationTokenFromJWT(jwt);

                log.debug("Populated authentication for JWT: {}", authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                

            } catch (JwtException e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    // This method determines which requests should not be filtered by this JWT filter.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/users/register") || path.startsWith("/api/users/login") ||
               path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }
}