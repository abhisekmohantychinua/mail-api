package dev.abhisek.mailsenderapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ElementCollection
    private List<String> receivers = new ArrayList<>();
    private String subject;
    private String message;
    @ElementCollection
    private List<String> paths = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    private User user;

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

