package com.funmicode.service;

import com.funmicode.data.model.User;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;
import com.funmicode.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public CreateSignupResponse signup(CreateSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = mapper.mapUserRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        userRepository.save(user);
        CreateSignupResponse response = new CreateSignupResponse();
        response.setMessage("User created successfully as " + user.getRole().toString());
        response.setSuccess(true);
        return response;
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.format("%04d", new java.util.Random().nextInt(10000));
        user.setOtp(otp);
        user.setOtpExpiry(java.time.LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendOtpEmail(email, otp);

        return "OTP sent successfully to your email";
    }

    @Override
    public String resetPassword(com.funmicode.dto.request.ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Password reset successfully";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User savedUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), savedUser.getPassword())) {
            return new LoginResponse("Login successful", savedUser.getRole(), true);
        }

        // Lazy Migration: Check if the password matches as plain text
        if (savedUser.getPassword().equals(request.getPassword())) {
            // It matches plain text! Upgrade to hash.
            savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(savedUser);
            return new LoginResponse("Login successful", savedUser.getRole(), true);
        }

        throw new RuntimeException("Invalid credentials");
    }

}
