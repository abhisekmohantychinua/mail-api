package dev.abhisek.mailsenderapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JwtAuthRequest {
    private String username;
    private String password;
}
