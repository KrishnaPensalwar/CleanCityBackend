package com.cleancity.backend.controller;

import com.cleancity.backend.dto.request.ForgotPasswordRequest;
import com.cleancity.backend.dto.request.LoginRequest;
import com.cleancity.backend.dto.request.ResetPasswordRequest;
import com.cleancity.backend.dto.request.SignupRequest;
import com.cleancity.backend.dto.response.ApiResponse;
import com.cleancity.backend.dto.response.AuthResponse;
import com.cleancity.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.generatePasswordResetToken(request);
        return ResponseEntity.ok(new ApiResponse(true, "If an account with that email exists, an OTP will be sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse(true, "Password has been successfully reset."));
    }
}
