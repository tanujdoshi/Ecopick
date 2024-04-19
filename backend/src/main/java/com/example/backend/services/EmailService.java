package com.example.backend.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
