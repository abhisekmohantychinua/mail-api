package dev.abhisek.mailsenderapi.controller;

import dev.abhisek.mailsenderapi.entity.Mail;
import dev.abhisek.mailsenderapi.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
public class MailController {
    @Autowired
    private MailService mailService;



    @GetMapping("/mail")
    public ResponseEntity<List<Mail>> getMail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.mailService.getAllMail(token));
    }

    @PostMapping("/mail")
    public ResponseEntity<?> sendMail(@RequestHeader("Authorization") String token, @RequestBody Mail mail) {
        return ResponseEntity.ok(mailService.sendMail(token, mail));
    }
}
