package dev.abhisek.mailsenderapi.repository;

import dev.abhisek.mailsenderapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findUserByEmail(String email);
}
