package com.servesync.security;

import com.servesync.entity.user.User;
import com.servesync.exception.InvalidJwtException;
import com.servesync.exception.JwtExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private final CustomUserDetailsServiceImpl userDetailsService;

	public JwtUtils(CustomUserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration; // in milliseconds

	private SecretKey key;

	@PostConstruct
	public void init() {

		logger.info("Initializing JWT secret key with expiration: {} ms", expiration);

		if (secret.length() < 32) {
			logger.error("JWT secret is too short. Must be at least 32 characters for HS256.");
			throw new IllegalArgumentException("JWT secret must be at least 32 characters");
		}
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	/**
	 * Generates JWT token using authenticated user info
	 */
	public String generateJwtToken(Authentication authentication) {
		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);

		return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
				.claim("userId", userPrincipal.getId()).claim("email", userPrincipal.getUsername())
				.claim("authorities", getAuthoritiesAsStringList(userPrincipal.getAuthorities()))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Validates JWT and returns claims if valid
	 */
	public Claims validateJwtToken(String jwtToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			logger.warn("JWT token expired: {}", e.getMessage());
			throw new JwtExpiredException("Token has expired");
		} catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.UnsupportedJwtException
				| io.jsonwebtoken.security.SecurityException e) {
			logger.warn("Invalid JWT token: {}", e.getMessage());
			throw new InvalidJwtException("Invalid token");
		}
	}

	/**
	 * Extracts username (email) from claims
	 */
	public String getUserNameFromJwtToken(Claims claims) {
		return claims.getSubject();
	}

	/**
	 * Converts GrantedAuthority list to string list for JWT claim
	 */
	private List<String> getAuthoritiesAsStringList(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}

	/**
	 * Converts string list from JWT claim back to GrantedAuthority list
	 */
	public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
		List<String> authorities = claims.get("authorities", List.class);
		if (authorities == null) {
			logger.warn("No authorities found in JWT claims");
			return Collections.emptyList();
		}
		return authorities.stream().filter(Objects::nonNull).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	/**
	 * Used in JWT Filter to convert JWT back into Authentication object
	 */
	public Authentication populateAuthenticationTokenFromJWT(String jwt) {
		Claims claims = validateJwtToken(jwt);
		String email = getUserNameFromJwtToken(claims);

		// Load full user details so principal is CustomUserDetails
		CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

		// Optionally, you can verify the authorities from token vs userDetails if
		// needed

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

		logger.debug("JWT authentication created for user id: {}", userDetails.getId());

		return authentication;
	}
}
