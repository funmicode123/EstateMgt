package com.funmicode.utils;

import com.funmicode.data.model.ExitPass;
import com.funmicode.data.model.User;
import com.funmicode.data.model.UserRole;
import com.funmicode.data.model.VisitingLog;
import com.funmicode.data.repository.UserRepository;
import com.funmicode.dto.request.CreateSignupRequest;
import com.funmicode.dto.request.ExitPassRequest;
import com.funmicode.dto.request.LoginRequest;
import com.funmicode.dto.request.OtpRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class Mapper {

    private final UserRepository userRepository;

    public Mapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User mapUserRequest(CreateSignupRequest request) {
        User user = new User();
        user.setEmail(request.getEmail()); 
        user.setPassword(request.getPassword());
        user.setRole(UserRole.fromString(request.getRole()));
        return user;
    }

    public User mapLoginRequest(LoginRequest request) {
       Optional<User> savedUser= userRepository.findByEmail(request.getEmail());
       return savedUser.isPresent()? savedUser.get():null;
    }

    public VisitingLog mapOtpRequest(OtpRequest request) {
        VisitingLog log = new VisitingLog();
        log.setResidentEmail(request.getResidentEmail());
        log.setVisitorName(request.getVisitorName());
        return log;
    }

    public ExitPass mapExitPassRequest(ExitPassRequest request) {
        ExitPass exitPass = new ExitPass();
        exitPass.setVisitorName(request.getVisitorName());
        exitPass.setResidentEmail(request.getResidentEmail());
        exitPass.setExitTime(LocalDateTime.now());
        return exitPass;
    }

}
