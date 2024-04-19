package com.example.backend.services.impl;

import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.JwtAuthenticationResponse;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.entities.User;
import com.example.backend.entities.UserMeta;
import com.example.backend.entities.VerificationType;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.repository.UserMetaRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.AuthenticationService;
import com.example.backend.services.JWTService;
import com.example.backend.services.VerificationService;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// @NoArgsConstructor
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserMetaRepository userMetaRepository;
    private final VerificationService verificationService;

    /**
     * Registers new user and saves the details into the database
     * Sends verification mail to new users
     * @param signUpRequest DTO carrying all information required for signUP
     * @return new user object
     */
    @Override
    public User signUp(SignUpRequest signUpRequest) {

        User tempUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (tempUser != null) {
            throw new ApiRequestException("User already registered");
        }
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstname(signUpRequest.getFirstName());
        user.setLastname(signUpRequest.getLastName());
        user.setRole(signUpRequest.getRole());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        UserMeta userMeta = new UserMeta();
        userMeta.setUser(user);
        userMetaRepository.save(userMeta);
        VerificationType type = VerificationType.valueOf("VerifyEmail");
        verificationService.sendVerificationEmail(user, type);
        return user;

    }

    /**
     * sends reset password email to user on the registered email
     * @param resetPasswordRequest DTO with user emailID
     * @return
     */
    public String forgotPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail());
        if (user == null) {
            throw new ApiRequestException("User doesn't exist");
        }
        VerificationType type = VerificationType.valueOf("ResetPassword");
        verificationService.sendVerificationEmail(user, type);
        return "Please check email for password reset link";
    }

    /**
     * Authenticates user and creates a new JWT token and refresh token
     * @param signInRequest User authentication details
     * @return returns the JWT tokens and user details
     */
    public LoginResponse signIn(SignInRequest signInRequest) {

        UserMeta userMeta;
        // User tempUser =
        // userRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
        User tempUser = userRepository.findByEmail(signInRequest.getEmail());

        if (tempUser != null) {
            Boolean password = BCrypt.checkpw(signInRequest.getPassword(), tempUser.getPassword());

            if (password) {
                userMeta = userMetaRepository.findByUser(tempUser);
                if (userMeta.isVerified() == true) {
                    var jwt = jwtService.generateToken(tempUser);
                    var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), tempUser);

                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setToken(jwt);
                    loginResponse.setRefreshToken(refreshToken);
                    loginResponse.setEmail(tempUser.getEmail());
                    loginResponse.setFirstname(tempUser.getFirstname());
                    loginResponse.setLastname(tempUser.getLastname());
                    loginResponse.setRole(tempUser.getRole());
                    loginResponse.setWallet_balance(userMeta.getWallet_balance());
                    return loginResponse;
                } else {
                    throw new ApiRequestException("please varify your email");
                }
            } else {
                throw new ApiRequestException("Wrong username or password");
            }
        } else {
            throw new ApiRequestException("Wrong username or password");
        }
    }

    /**
     * Refreshes user token in case of expiry
     * @param refreshTokenRequest contains the refresh token
     * @return generated token
     */
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), userDetails)) {
            var jwt = jwtService.generateToken(userDetails);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
