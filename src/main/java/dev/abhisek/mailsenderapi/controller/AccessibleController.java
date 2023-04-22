package dev.abhisek.mailsenderapi.controller;

import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.security.JwtAuthRequest;
import dev.abhisek.mailsenderapi.security.JwtAuthResponse;
import dev.abhisek.mailsenderapi.security.JwtTokenHelper;
import dev.abhisek.mailsenderapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessibleController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<String> HII() {

        return ResponseEntity.ok("Welcome");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println("/register called");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userService.addUser(user);
        return ResponseEntity.ok(this.userService.addUser(user));
    }

    @GetMapping("/token")
    public ResponseEntity<String> welcome() {
        System.out.println("/token called");
        return ResponseEntity.ok("Please login with username and password ");
    }


    @PostMapping("/token")
    public ResponseEntity<JwtAuthResponse> getToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
        System.out.println("/token called");
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
        if (passwordEncoder.matches(jwtAuthRequest.getPassword(), userDetails.getPassword())) {
            String token = this.jwtTokenHelper.generateToken(jwtAuthRequest.getUsername());
            System.out.println("generated token : " + token);
            return ResponseEntity.ok(new JwtAuthResponse(token));
        } else {
            System.out.println("password does not match " + jwtAuthRequest.getPassword() + ":" + userDetails.getPassword());
            throw new UsernameNotFoundException("User not found");
        }
    }

}
