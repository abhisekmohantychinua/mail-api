package dev.abhisek.mailsenderapi.service;

import dev.abhisek.mailsenderapi.entity.Mail;
import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.function.Mailer;
import dev.abhisek.mailsenderapi.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MailService {
    @Autowired
    private UserService userService;
    @Autowired
    private Mailer mailer;
    @Autowired
    private MailRepository mailRepository;


    public Optional<String> sendMail(String token, Mail mail) {
        User user = this.userService.getUserByJwtToken(token);
        System.out.println("MailService : "+user);
        mail.setUser(user);
        try {
            this.mailRepository.save(mail);
            return this.mailer.sendMail(mail, user);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Mail> getAllMail(String token) {
        return this.mailRepository.findByUser(this.userService.getUserByJwtToken(token));
    }
}
