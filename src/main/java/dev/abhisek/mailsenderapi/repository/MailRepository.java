package dev.abhisek.mailsenderapi.repository;

import dev.abhisek.mailsenderapi.entity.Mail;
import dev.abhisek.mailsenderapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail,Integer> {
    public List<Mail> findByUser(User user);
}
