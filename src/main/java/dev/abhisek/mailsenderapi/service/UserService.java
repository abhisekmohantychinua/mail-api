package dev.abhisek.mailsenderapi.service;

import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.repository.UserRepository;
import dev.abhisek.mailsenderapi.security.JwtTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    public User addUser(User user) {
        return this.userRepository.save(user);
    }


    public User getUserByJwtToken(String token) {
        return this.userRepository.findUserByEmail(this.jwtTokenHelper.getUsernameFromToken(token));
    }


}
