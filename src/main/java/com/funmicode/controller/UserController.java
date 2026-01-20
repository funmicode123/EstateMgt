package com.funmicode.controller;

import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;
import com.funmicode.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<CreateSignupResponse> register(@Valid @RequestBody CreateSignupRequest request) {
        CreateSignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody com.funmicode.dto.request.ForgotPasswordRequest request) {
        String response = userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody com.funmicode.dto.request.ResetPasswordRequest request) {
        String response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
