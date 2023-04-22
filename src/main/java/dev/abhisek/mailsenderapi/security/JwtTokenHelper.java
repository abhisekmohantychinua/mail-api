package dev.abhisek.mailsenderapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenHelper {
    // Jwt Secret
    @Value("${env.JWT_SECRET}")
    private String SECRET;

    // Jwt Expiration in millis
    @Value("${env.JWT_TOKEN_VALIDITY}")
    private Long JWT_TOKEN_VALIDITY;


    private Claims parseToken(String token) {
        System.out.println("Secret : " + this.SECRET);
        System.out.println("Validity : " + this.JWT_TOKEN_VALIDITY);
        // Create JwtParser
        Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));
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
        System.out.println("Secret : " + this.SECRET);
        System.out.println("Validity : " + this.JWT_TOKEN_VALIDITY);
        // Generate token
        Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(SECRET.getBytes()));
        var currentDate = new Date();
        var expiration = new Date(currentDate.getTime() + JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiration)
                .signWith(key)
                .compact()
                .replace("+", "-").replace("/", "_").replace("=", "");
    }
}
