package dev.abhisek.mailsenderapi.service;

import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.repository.UserRepository;
import dev.abhisek.mailsenderapi.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtility jwtUtility;

    public void addUser(User user) {
        this.userRepository.save(user);
    }


    public Optional<User> getUserByJwtToken(String token) {
        String email = this.jwtUtility.extractUsername(token);
        System.out.println("Email : " + email);
        return this.userRepository.findUserByEmail(email);
    }


}
