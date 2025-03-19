package com.funmicode.controller;

import com.funmicode.data.model.VisitingLog;
import com.funmicode.dto.request.OtpValidRequest;
import com.funmicode.dto.response.OtpValidResponse;
import com.funmicode.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class SecurityController {
    @Autowired
    private SecurityService securityService;

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpValidResponse> validateOtp(@Valid @RequestBody OtpValidRequest otpRequest) {
        OtpValidResponse valid = securityService.validateOtp(otpRequest);
        return ResponseEntity.ok(valid);
    }



    @GetMapping("/view")
    public ResponseEntity<List> viewVisitingLog() {
        List<VisitingLog> log = securityService.getVisitingLogs();
        return ResponseEntity.ok(log);
    }
}
