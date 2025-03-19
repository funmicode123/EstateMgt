package com.funmicode.service;

import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.response.CreateSignupResponse;
import com.funmicode.dto.response.LoginResponse;

public interface UserService {
    CreateSignupResponse signup(CreateSignupRequest request);
    LoginResponse login(LoginRequest request);
}
