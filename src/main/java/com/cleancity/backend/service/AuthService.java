package com.cleancity.backend.service;

import com.cleancity.backend.config.JwtConfig;
import com.cleancity.backend.dto.request.ForgotPasswordRequest;
import com.cleancity.backend.dto.request.LoginRequest;
import com.cleancity.backend.dto.request.ResetPasswordRequest;
import com.cleancity.backend.dto.request.SignupRequest;
import com.cleancity.backend.dto.response.AuthResponse;
import com.cleancity.backend.exception.CustomException;
import com.cleancity.backend.model.User;
import com.cleancity.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email is already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        user.getProfile().put("firstName", request.getFirstName());
        user.getProfile().put("lastName", request.getLastName());
        user.getMetadata().put("createdAt", System.currentTimeMillis());
        user.getMetadata().put("schemaVersion", 1);

        user = userRepository.save(user);

        String token = jwtConfig.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new CustomException("Invalid credentials");
        }

        if ("LOCKED".equals(user.getStatus()) || "INACTIVE".equals(user.getStatus())) {
            throw new CustomException("Account is " + user.getStatus().toLowerCase());
        }

        // Update last login
        user.getMetadata().put("lastLoginAt", System.currentTimeMillis());
        userRepository.update(user);

        String token = jwtConfig.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getEmail());
    }

    public void generatePasswordResetToken(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Generate a 6-digit OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000); // 15 mins

            user.getAuth().put("resetToken", otp);
            user.getAuth().put("resetTokenExpiry", expiryTime);
            
            userRepository.update(user);
            
            // TODO: Integrations to genuinely send email (e.g. JavaMailSender, Sendgrid)
            // System.out.println("Generated OTP for " + user.getEmail() + " is " + otp);
        } else {
            // Do not throw an error to prevent user enumeration attacks
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid email or OTP"));

        String savedToken = (String) user.getAuth().get("resetToken");
        Long tokenExpiry = (Long) user.getAuth().get("resetTokenExpiry");

        if (savedToken == null || !savedToken.equals(request.getOtp())) {
            throw new CustomException("Invalid OTP");
        }

        if (tokenExpiry == null || tokenExpiry < System.currentTimeMillis()) {
            throw new CustomException("OTP has expired");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.getAuth().remove("resetToken");
        user.getAuth().remove("resetTokenExpiry");

        userRepository.update(user);
    }
}
