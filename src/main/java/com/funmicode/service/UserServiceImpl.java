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

    @Override
    public CreateSignupResponse signup(CreateSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = mapper.mapUserRequest(request);
        userRepository.save(user);
        CreateSignupResponse response = new CreateSignupResponse();
        response.setMessage("User created successfully as " + user.getRole().toString());
        response.setSuccess(true);
        return response;
    }

//    @Override
//    public LoginResponse login(LoginRequest request) {
//        String email = request.getEmail();
//        String password = request.getPassword();
//        String userRole= request.getRole();
//        Optional<User> savedUser = userRepository.findByEmail(email);
//        LoginResponse loginResponse = new LoginResponse();
//
//        if (savedUser.isEmpty()) {
//            return new LoginResponse("User not found", false);
//        }
//        User user = savedUser.get();
//
//        if(user.getPassword().equals(password)) {
//            return new LoginResponse("Login successfully", true);
//        }
//
//        else {
//            return new LoginResponse("Wrong password",false);
//        }
//    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User savedUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!savedUser.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new LoginResponse("Login successful", savedUser.getRole(), true);
    }

}
