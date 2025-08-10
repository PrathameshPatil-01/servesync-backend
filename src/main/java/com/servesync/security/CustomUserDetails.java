package com.servesync.security;

import com.servesync.entity.role.Role;
import com.servesync.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles() != null ? user.getRoles() : Collections.emptySet();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // password_hash field
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // using email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Change if you have expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Change if you track lock state
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Change if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // You can add `isEnabled` field to user if needed
    }

	public Long getId() {

		return user.getId();
	}
}
