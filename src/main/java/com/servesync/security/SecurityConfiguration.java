package com.servesync.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables @PreAuthorize and other annotations
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .cors(Customizer.withDefaults())
        	.csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // Public access endpoints
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/users/register",
                    "/api/users/login"
                ).permitAll()

                // Public GET APIs
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                // Role-based access
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
                .requestMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")

                // Admin-only POST access
                .requestMatchers(HttpMethod.POST, "/api").hasRole("ADMIN")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )

            // Stateless session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Disable default form login
            .formLogin(form -> form.disable())

            // Enable HTTP Basic (optional)
            .httpBasic(Customizer.withDefaults())

            // JWT filter before username-password auth
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
