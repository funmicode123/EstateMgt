package com.funmicode.service;

import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;

public interface UserService {
    CreateSignupResponse signup(CreateSignupRequest request);
    CreateSignupResponse createSecurity(CreateSignupRequest request);
    String approveResident(String email);
    LoginResponse login(LoginRequest request);
    String forgotPassword(String email);
    String resetPassword(com.funmicode.dto.request.ResetPasswordRequest request);
    com.funmicode.dto.response.ProfileResponse getProfile(String email);
    com.funmicode.dto.response.ProfileResponse updateProfile(String email, com.funmicode.dto.request.ProfileUpdateRequest request);
}
