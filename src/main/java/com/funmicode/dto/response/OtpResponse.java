package com.funmicode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class OtpResponse {
    private String otp;
    private String message;
    private LocalDateTime expiresTime;
    private boolean success;
}
