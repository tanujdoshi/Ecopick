package com.example.backend.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.backend.entities.VerificationType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;
import com.example.backend.entities.VerificationCode;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.UserMetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VerificationCodeRepository;
import com.example.backend.services.EmailService;
import com.example.backend.services.VerificationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationServiceImplementation implements VerificationService {
    static final int ADD_MINUTES_EXPIRY = 30;

    // private final UserMeta userMeta;
    private final UserRepository userRepository;
    private final UserMetaRepository userMetaRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${frontend.endpoint}")
    private String frontendEndpoint;

    /**
     * Verifies the code sent through the email and performs the necessary changes
     * @param code verification code sent through email
     * @param email email id to which it was sent
     * @param newPassword new password to reset to
     * @param type type of verification process
     * @return Success confirmation
     */
    @Override
    public String verifyAndUpdate(String code, String email, String newPassword, String type) {
        try {
            VerificationType verificationType = VerificationType.valueOf(type);
            switch (verificationType) {
                case VerifyEmail:
                    verify(code, email);
                    return "User verified succesfully";
                case ResetPassword:
                    resetPassword(code, email, newPassword);
                    return "Password reset succesfully";
                default:
                    throw new ApiRequestException("Invalid type");
            }
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    /**
     * sends verification email to the email giving by the user during signup
     * @param code code that needs to be sent to the user
     * @param email email ID of the user
     */
    @Override
    public void verify(String code, String email) {
        // User user = userRepository.findByEmail(email).orElseThrow();
        User user = userRepository.findByEmail(email);
        UserMeta userMeta = userMetaRepository.findByUser(user);
        if (user == null) {
            throw new ApiRequestException("Not able to find user");
        }

        if (userMeta.isVerified()) {
            throw new ApiRequestException("User is already verified");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByCodeAndEmail(code, email);
        if (verificationCode == null) {
            throw new ApiRequestException("Invalid verification code");
        }

        if (verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ApiRequestException("Verification code has expired");
        }

        userMeta.setVerified(true);
        userMetaRepository.save(userMeta);
        verificationCodeRepository.delete(verificationCode);
    }

    /**
     * Function sends email to the emailID of the user for resetting password
     * @param code Verification code
     * @param email email ID of the user
     * @param newPassword the new password
     */
    @Override
    public void resetPassword(String code, String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ApiRequestException("Not able to find user");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByCodeAndEmail(code, email);
        if (verificationCode == null) {
            throw new ApiRequestException("Invalid verification code");
        }

        if (verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ApiRequestException("Verification code has expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        verificationCodeRepository.delete(verificationCode);
    }

    /**
     * sends the verification email to the user
     * @param user user
     * @param type type of operation for which verification is required
     */
    @Override
    public void sendVerificationEmail(User user, VerificationType type) {
        UserMeta userMeta = userMetaRepository.findByUser(user);
        if (user == null) {
            throw new ApiRequestException("User not found!");
        }

        if (userMeta.isVerified() && type != VerificationType.valueOf("ResetPassword")) {
            throw new ApiRequestException("User already verified");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(user.getEmail());
        if (verificationCode != null && verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            verificationCodeRepository.delete(verificationCode);
            verificationCode = null;
        }

        if (verificationCode == null) {
            String code = UUID.randomUUID().toString();
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(ADD_MINUTES_EXPIRY);
            verificationCode = new VerificationCode(code, user.getEmail(), expiryTime, type);
            verificationCodeRepository.save(verificationCode);
        }

        String url = generateUrl(type);

        String verificationUrl = String.format(
                url,
                user.getEmail(),
                verificationCode.getCode(),
                verificationCode.getVerificationType());
        if (type == VerificationType.valueOf("VerifyEmail")) {
            String subject = "Verify your email";
            String body = "Please click on this link to verify your email: " + verificationUrl;
            emailService.sendEmail(user.getEmail(), subject, body);
        } else if (type == VerificationType.valueOf("ResetPassword")) {
            String subject = "Reset your password";
            String body = "Please click on this link to reset your Password: " + verificationUrl;
            emailService.sendEmail(user.getEmail(), subject, body);
        }
    }

    /**
     * generates the URL that is sent in the email
     * @param type type of operation
     * @return returns the string url that is sent in the email
     */
    public String generateUrl(VerificationType type) {
        String url = "";
        if (type == VerificationType.VerifyEmail) {
            url = frontendEndpoint + "/verify-email?email=%s&code=%s&type=%s";
        } else if (type == VerificationType.ResetPassword) {
            url = frontendEndpoint + "/reset-password?email=%s&code=%s&type=%s";
        }
        System.out.println("HEREE?" + url);
        return url;
    }
}
