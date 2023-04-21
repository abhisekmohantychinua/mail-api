package dev.abhisek.mailsenderapi.controller;

import dev.abhisek.mailsenderapi.entity.Mail;
import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.security.JwtAuthRequest;
import dev.abhisek.mailsenderapi.security.JwtAuthResponse;
import dev.abhisek.mailsenderapi.security.JwtTokenHelper;
import dev.abhisek.mailsenderapi.service.MailService;
import dev.abhisek.mailsenderapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
public class HomeController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userService.addUser(user);
        return ResponseEntity.ok(this.userService.addUser(user));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest jwtAuthRequest) {

        Authentication authentication =
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword())
                );

        if (authentication.isAuthenticated()) {
            System.out.println(jwtAuthRequest);
            return ResponseEntity.ok(new JwtAuthResponse(
                    this.jwtTokenHelper.generateToken(this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername()))
            ));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    @GetMapping("/mail")
    public ResponseEntity<List<Mail>> getMail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.mailService.getAllMail(token));
    }

    @PostMapping("/mail")
    public ResponseEntity<?> sendMail(@RequestHeader("Authorization") String token, @RequestBody Mail mail) {
        return ResponseEntity.ok(mailService.sendMail(token, mail));
    }
}
