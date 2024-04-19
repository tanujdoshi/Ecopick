package com.example.backend.Services;

import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;
import com.example.backend.entities.VerificationCode;
import com.example.backend.entities.VerificationType;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.UserMetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VerificationCodeRepository;
import com.example.backend.services.EmailService;
import com.example.backend.services.impl.VerificationServiceImplementation;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.PrivateKey;
import java.time.LocalDateTime;

public class VerificationServiceImplTest {
    static final int ADD_MINUTES_EXPIRY = 30;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMetaRepository userMetaRepository;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private PasswordEncoder passwordEcoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationServiceImplementation verificationServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerifyEmailSuccess() {
        // ARRANGE
        VerificationType verificationType = VerificationType.VerifyEmail;
        String code = "123";
        String email = "test@gmail.com";
        UserMeta userMeta = new UserMeta();
        userMeta.setVerified(false);
        User user = new User();
        user.setEmail(email);
        user.setUserMeta(userMeta);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(ADD_MINUTES_EXPIRY));

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMetaRepository.findByUser(user)).thenReturn(userMeta);
        when(verificationCodeRepository.findByCodeAndEmail(code, email)).thenReturn(verificationCode);

        // ACT
        verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType));

        // ASSERT
        assertEquals(true, userMeta.isVerified());
        verify(userMetaRepository, times(1)).save(any(UserMeta.class));
        verify(verificationCodeRepository, times(1)).delete(any(VerificationCode.class));

    }

    @Test
    public void testVerifyEmailNoUserFound() {
        // ARRANGE
        String code = "123";
        String email = "test@gmail.com";
        VerificationType verificationType = VerificationType.VerifyEmail;

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(null);

        // ASSERT
        assertThrows(
                ApiRequestException.class,
                () -> verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType)));
    }

    @Test
    public void testUserAlreadyVerified() {
        // ARRANGE
        VerificationType verificationType = VerificationType.VerifyEmail;
        String code = "123";
        String email = "test@gmail.com";
        UserMeta userMeta = new UserMeta();
        userMeta.setVerified(true);
        User user = new User();
        user.setEmail(email);
        user.setUserMeta(userMeta);

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMetaRepository.findByUser(user)).thenReturn(userMeta);

        // ASSERT
        assertThrows(
                ApiRequestException.class,
                () -> verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType)));
    }

    @Test
    public void testExpiredVerificationCode() {
        // ARRANGE
        VerificationType verificationType = VerificationType.VerifyEmail;
        String code = "123";
        String email = "test@gmail.com";
        UserMeta userMeta = new UserMeta();
        userMeta.setVerified(false);
        User user = new User();
        user.setEmail(email);
        user.setUserMeta(userMeta);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setExpiryTime(LocalDateTime.now().minusMinutes(ADD_MINUTES_EXPIRY));

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMetaRepository.findByUser(user)).thenReturn(userMeta);
        when(verificationCodeRepository.findByCodeAndEmail(code, email)).thenReturn(verificationCode);

        // ASSERT
        assertThrows(
                ApiRequestException.class,
                () -> verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType)));
    }

    @Test
    public void testResetPasswordSuccess() {
        // ARRANGE
        String code = "123";
        VerificationType verificationType = VerificationType.ResetPassword;
        String newPassword = "TestPassword";
        String email = "test@gmail.com";
        User user = new User();
        user.setEmail(email);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(ADD_MINUTES_EXPIRY));

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(verificationCodeRepository.findByCodeAndEmail(code, email)).thenReturn(verificationCode);

        verificationServiceImpl.verifyAndUpdate(code, email, newPassword, String.valueOf(verificationType));

        // ASSERT
        String encodedPassword = passwordEcoder.encode(newPassword);
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(verificationCodeRepository, times(1)).delete(any(VerificationCode.class));
    }

    @Test
    public void testResetPasswordUserNotFound() {
        // ARRANGE
        String code = "123";
        String email = "test@gmail.com";
        VerificationType verificationType = VerificationType.ResetPassword;

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(null);

        // ASSERT
        assertThrows(
                ApiRequestException.class,
                () -> verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType)));
    }

    @Test
    public void testExpiredVerificationResetPassword() {
        // ARRANGE
        VerificationType verificationType = VerificationType.ResetPassword;
        String code = "123";
        String email = "test@gmail.com";
        UserMeta userMeta = new UserMeta();
        userMeta.setVerified(false);
        User user = new User();
        user.setEmail(email);
        user.setUserMeta(userMeta);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setExpiryTime(LocalDateTime.now().minusMinutes(ADD_MINUTES_EXPIRY));

        // ACT
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMetaRepository.findByUser(user)).thenReturn(userMeta);
        when(verificationCodeRepository.findByCodeAndEmail(code, email)).thenReturn(verificationCode);

        // ASSERT
        assertThrows(
                ApiRequestException.class,
                () -> verificationServiceImpl.verifyAndUpdate(code, email, null, String.valueOf(verificationType)));

    }

}
