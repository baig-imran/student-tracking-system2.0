package com.sts.service.impl;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sts.service.interfaces.JWTService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTServiceImpl implements JWTService {

    private static final String SECRET_STRING = "your-256-bit-secret-key-must-be-at-least-32-characters-long";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // Add custom claims if needed
        claims.put("role", "USER"); // Example claim

        return Jwts.builder()
                .claims(claims) // Add claims
                .subject(username) // Set subject (username)
                .issuedAt(new Date(System.currentTimeMillis())) // Set issued at time
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10000)) // Set expiration time (1 hour)
                .signWith(SECRET_KEY) // Sign the token with the secret key
                .compact(); // Compact into a string
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Helper method to extract a specific claim from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Helper method to extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // Verify the token with the secret key
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Helper method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Helper method to extract the expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}