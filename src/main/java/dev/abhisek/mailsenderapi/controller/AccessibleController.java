package dev.abhisek.mailsenderapi.controller;

import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.security.JwtAuthRequest;
import dev.abhisek.mailsenderapi.security.JwtAuthResponse;
import dev.abhisek.mailsenderapi.service.UserService;
import dev.abhisek.mailsenderapi.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessibleController {

    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public ResponseEntity<String> HII() {
        return ResponseEntity.ok("Welcome");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println("/register called");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userService.addUser(user);
        return ResponseEntity.accepted().body("Saved successfully");
    }

    @GetMapping("/token")
    public ResponseEntity<String> welcome() {
        System.out.println("/token called");
        return ResponseEntity.ok("Please login with username and password ");
    }


    @PostMapping("/token")
    public ResponseEntity<JwtAuthResponse> getToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUsername(),
                this.passwordEncoder.encode(jwtAuthRequest.getPassword())));
        return ResponseEntity.ok(new JwtAuthResponse(this.jwtUtility.generateToken(jwtAuthRequest.getUsername())));
    }

}
