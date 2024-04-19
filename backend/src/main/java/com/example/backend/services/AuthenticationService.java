package com.example.backend.services;

import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.JwtAuthenticationResponse;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.entities.User;

public interface AuthenticationService {
    User signUp(SignUpRequest signUpRequest);

    String forgotPassword(ResetPasswordRequest resetPasswordRequest);

    LoginResponse signIn(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
