package dev.abhisek.mailsenderapi.config;

import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = this.userRepository.findUserByEmail(username);
        return userInfo.map(CustomUserDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}
