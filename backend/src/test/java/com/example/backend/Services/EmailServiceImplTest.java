package com.example.backend.Services;

import static org.mockito.Mockito.*;
import com.example.backend.services.impl.EmailServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImplTest {
    @BeforeEach
    public void Setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private JavaMailSender JavaMailSenderMock;

    @InjectMocks
    private EmailServiceImplementation emailTest;

    @Test
    public void testSendEmailSuccess() {
        String to = "tvd@gmail.com";
        String subject = "Verify your email";
        String body = "Please click on this link to verify your email";

        emailTest.sendEmail(to, subject, body);

        verify(JavaMailSenderMock).send(any(SimpleMailMessage.class));
    }
}
