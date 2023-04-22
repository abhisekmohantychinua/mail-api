package dev.abhisek.mailsenderapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JwtAuthResponse {
    private String token;
}
