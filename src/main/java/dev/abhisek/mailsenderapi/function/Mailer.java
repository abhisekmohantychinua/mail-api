package dev.abhisek.mailsenderapi.function;

import dev.abhisek.mailsenderapi.entity.Mail;
import dev.abhisek.mailsenderapi.entity.User;
import dev.abhisek.mailsenderapi.security.JwtTokenHelper;
import dev.abhisek.mailsenderapi.service.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;


@Service
public class Mailer {


    public Optional<String> sendMail(Mail mail, User user) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user.getEmail(), user.getPassword());
            }
        });
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);


        try {
            mimeMessage.addRecipients(Message.RecipientType.TO, mail.getReceivers().stream()
                    .map(receiver -> {
                        try {
                            return new InternetAddress(receiver);
                        } catch (AddressException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .toArray(InternetAddress[]::new));
            mimeMessage.setFrom(user.getEmail());
            mimeMessage.setSubject(mail.getSubject());

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart text = new MimeBodyPart();
            text.setText(mail.getMessage());

            multipart.addBodyPart(text);
            mail.getPaths().forEach(path -> {
                try {
                    MimeBodyPart bodyPart = new MimeBodyPart();
                    bodyPart.attachFile(new File(path));
                    multipart.addBodyPart(bodyPart);
                } catch (MessagingException | NullPointerException | IOException e) {
                    throw new RuntimeException(e);
                }
            });


            mimeMessage.setContent(multipart);

            Transport.send(mimeMessage);
            return Optional.of("Success");

        } catch (MessagingException e) {
            e.printStackTrace();
            return Optional.of(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }
}
