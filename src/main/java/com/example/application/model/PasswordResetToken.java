package com.example.application.model;


import lombok.Data;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "passwordresettokens")
@Data
public class PasswordResetToken extends BaseEntity{

    private static final int EXPIRATION = 60 * 24;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        expiryDate = LocalDateTime.now().plusSeconds(EXPIRATION);
        setCreated(LocalDateTime.now());
        setUpdated(LocalDateTime.now());
    }

    public PasswordResetToken() {
    }
}
