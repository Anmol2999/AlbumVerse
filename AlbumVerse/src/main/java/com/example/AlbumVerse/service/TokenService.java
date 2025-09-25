package com.example.AlbumVerse.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
 
    // Final variable for JWT encoder
    private final JwtEncoder encoder;

    // Constructor for dependency injection of JwtEncoder
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
 
    // Method to generate JWT token based on authentication details
    public String generateToken(Authentication authentication) {
        // Get the current timestamp
        Instant now = Instant.now();
 
        // Extract and concatenate user authorities
        String scope = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(" "));
 
        // Build JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self") // Issuer of the token
            .issuedAt(now) // Token creation time
            .expiresAt(now.plus(1, ChronoUnit.HOURS)) // Token expiry time
            .subject(authentication.getName()) // Subject (username)
            .claim("scope", scope) // User roles
            .build();
 
        // Encode and return the JWT token
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}