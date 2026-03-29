package com.server.lms.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtUtils {

    private final SecretKey SECRET_KEY;

    @Value("${spring.security.jwt.expiration}")
    private Duration expiration;


    JwtUtils(@Value("${spring.security.jwt.secret.key}") String SECRET_KEY) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject /* Send function reference */); // getSubject is function to return user identifier <must be unique> (username / email)
    }


    public String extractAuthorities(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("authorities", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract JWT Payload
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateToken(UserDetails userDetails) {
        Set<String> roles = new HashSet<>();

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            roles.add(grantedAuthority.getAuthority());
        }


        return Jwts.builder()
                .claim("authorities", String.join(",", roles))
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration.toMillis()))
                .signWith(SECRET_KEY)
                .compact();
    }


    // Check token expiration & check if token  belong to the login user or not
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenSignatureCorrect(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // expiration_date.before(current_date) ? expired : valid;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
