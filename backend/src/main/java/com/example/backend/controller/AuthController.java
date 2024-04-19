package com.example.backend.controller;

import com.example.backend.dto.request.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.request.SignInRequest;
import com.example.backend.dto.request.SignUpRequest;
import com.example.backend.dto.response.JwtAuthenticationResponse;
import com.example.backend.dto.response.LoginResponse;
import com.example.backend.entities.User;
import com.example.backend.services.AuthenticationService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * End point for userSignup
     * @param signUpRequest contains the user information
     * @return returns the user information to the frontend on success
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }

    /**
     * Endpoint for initiating the password reset
     * @param resetPasswordRequest contains the email ID
     * @return returns success or failure to the frontend
     */
    @PostMapping("/ResetPasswordReq")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(authenticationService.forgotPassword(resetPasswordRequest));
    }

    /**
     * Endpoint for the user to signin
     * @param signInRequest Contains the information required for sign in
     * @return returns the JWT token and refresh token
     */
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@RequestBody @Valid SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

    /**
     * Endpoint for refreshing the token
     * @param refreshTokenRequest contains the refresh token
     * @return returns the new tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
