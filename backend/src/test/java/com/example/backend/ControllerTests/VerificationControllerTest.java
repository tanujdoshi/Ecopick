package com.example.backend.ControllerTests;

import com.example.backend.controller.VerificationController;
import com.example.backend.services.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class VerificationControllerTest {

    @InjectMocks
    private VerificationController verificationController;

    @Mock
    private VerificationService verificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void VerifyEmail() {
        // Mocked data
        String code = "verification_code";
        String email = "user@example.com";
        String type = "some_type";
        String newPassword = "new_password";
        String message = "Verification successful";

        // Mocking verificationService.verifyAndUpdate() to return a success message
        when(verificationService.verifyAndUpdate(code, email, newPassword,
                type)).thenReturn(message);

        // Call the method under test
        ResponseEntity<String> response = verificationController.verify(code, email,
                type, newPassword);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
