package com.mindfire.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    /**
     * Description: Generates a JWT token for an authenticated user.
     *
     * @param authentication The authentication object containing user details.
     * @return A JWT token as a String.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .claim("roles",roles)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    /**
     * Description: Retrieves the signing key for JWT encryption.
     *
     * @return The secret key used for signing the JWT.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    /**
     * Description: Extracts the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Description: Validates the given JWT token.
     *
     * @param token The JWT token.
     * @return A boolean indicating whether the token is valid.
     */
    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
    }

}
