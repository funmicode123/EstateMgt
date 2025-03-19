package com.funmicode.controller;

import com.funmicode.dto.request.ExitPassRequest;
import com.funmicode.dto.request.OtpRequest;
import com.funmicode.dto.response.ExitPassResponse;
import com.funmicode.dto.response.OtpResponse;
import com.funmicode.service.ResidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resident")
public class ResidentController {
    @Autowired
    private ResidentService residentService;

    @PostMapping("/generate-otp")
    public ResponseEntity<OtpResponse> generateOtp(@Valid @RequestBody OtpRequest otpRequest) {
        OtpResponse otp = residentService.generateOtpForVisitor(otpRequest);
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/exit")
    public ResponseEntity<ExitPassResponse> generateExitPass(@Valid @RequestBody ExitPassRequest exitPassRequest) {
        return ResponseEntity.ok(residentService.generateExitPass(exitPassRequest));
    }
}
