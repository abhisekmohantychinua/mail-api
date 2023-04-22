package dev.abhisek.mailsenderapi.security;

import ch.qos.logback.core.encoder.EchoEncoder;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {
    // Jwt Secret
    private final String secret = "VGhpcyBpcyBhIHRlc3QgZnJvbSBiYXNlNjQgZW5jb2RlZA==";

    // Jwt Expiration in millis
    private final Long expiration = 300000L;


    private Claims parseToken(String token) {
        JwtParser jwtParser =
                Jwts
                        .parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                        .build();
        try {
            return jwtParser.parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException
                 | UnsupportedJwtException
                 | MalformedJwtException
                 | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean validateToken(String token) {
        return parseToken(token) != null;
    }

    public String getUsernameFromToken(String token) {
        // Get claims
        Claims claims = parseToken(token);
        // Extract subject
        if (claims != null) {

            return claims.getSubject();

        }
        return null;
    }

    public String generateToken(String username) {
        // Create a signing key
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        // Generate token
        var currentDate = new Date();
        var expiry = new Date(currentDate.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }
}
