package com.servesync.security;

import com.servesync.entity.role.Role;
import com.servesync.entity.user.User;
import com.servesync.enums.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testGenerateAndValidateJwtToken() {
        // Arrange: Create a mock User and CustomUserDetails
        Set<Role> roles = new HashSet<>();
        roles.add(new Role((short) 1, RoleName.ROLE_CUSTOMER, "Customer role", new HashSet<>()));
        User user = new User(1L, "john@example.com", "encodedPassword", roles);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );

        // Act: Generate and validate JWT
        String token = jwtUtils.generateJwtToken(auth);
        io.jsonwebtoken.Claims claims = jwtUtils.validateJwtToken(token);

        // Assert: Verify token contents
        assertNotNull(token, "Generated JWT should not be null");
        assertEquals("john@example.com", jwtUtils.getUserNameFromJwtToken(claims), "Username should match");
        assertEquals(
                List.of("ROLE_CUSTOMER"),
                jwtUtils.getAuthoritiesFromClaims(claims).stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                "Authorities should match"
        );
    }

    @Test
    void testPopulateAuthenticationTokenFromJWT() {
        // Arrange: Create a mock User and CustomUserDetails
        Set<Role> roles = new HashSet<>();
        roles.add(new Role((short) 1, RoleName.ROLE_CUSTOMER, "Customer role", new HashSet<>()));
        User user = new User(1L, "john@example.com", "encodedPassword", roles);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );

        // Act: Generate JWT and create Authentication from it
        String token = jwtUtils.generateJwtToken(auth);
        Authentication authentication = jwtUtils.populateAuthenticationTokenFromJWT(token);

        // Assert: Verify Authentication object
        assertEquals("john@example.com", authentication.getPrincipal(), "Principal should match username");
        assertEquals(
                List.of("ROLE_CUSTOMER"),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                "Authorities should match"
        );
        assertTrue(authentication.isAuthenticated(), "Authentication should be authenticated");
    }

    @Test
    void testValidateJwtTokenWithInvalidToken() {
        // Act & Assert: Expect InvalidJwtException for invalid token
        assertThrows(
                com.servesync.exception.InvalidJwtException.class,
                () -> jwtUtils.validateJwtToken("invalid.token.here"),
                "Invalid token should throw InvalidJwtException"
        );
    }
}
