package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String email;
    private LocalDateTime expiryTime;
    @Enumerated(EnumType.STRING)
    private VerificationType verificationType;

    public VerificationCode(String code, String email, LocalDateTime expiryTime, VerificationType type) {
        this.code = code;
        this.email = email;
        this.expiryTime = expiryTime;
        this.verificationType = type;
    }
}
