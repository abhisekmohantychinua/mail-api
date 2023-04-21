package dev.abhisek.mailsenderapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Integer id;
    @ElementCollection
    protected List<String> receivers = new ArrayList<>();
    protected String subject;
    protected String message;
    @ElementCollection
    protected List<String> paths = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    public Mail(List<String> receivers, String subject, String message, List<String> paths) {
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
        this.paths = paths;
    }

    public Mail(List<String> receivers, String subject, String message) {
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
    }
}

