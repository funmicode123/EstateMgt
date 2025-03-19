package com.funmicode.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class OtpValidRequest {
    @NotBlank(message = "Otp is required")
    private String otp;
}
