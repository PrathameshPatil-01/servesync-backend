package com.servesync.security;

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

import lombok.AllArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor

public class SecurityConfiguration {

	private final JwtFilter jwtFilter;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(requests -> requests
		    		.requestMatchers("/swagger-ui/**",
							"/v3/api-docs/**", "/users/register", "/users/login")
							.permitAll()
							.requestMatchers(HttpMethod.GET, "/api/**")
							.permitAll()
							.requestMatchers(HttpMethod.POST, "/api")
							.hasRole("ADMIN")
							.anyRequest()
							.authenticated());

		http.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.formLogin(form -> form.disable());

		http.httpBasic(Customizer.withDefaults());

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config ) throws Exception {
		return config.getAuthenticationManager();
	}
}