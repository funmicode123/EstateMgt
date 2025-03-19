package com.funmicode.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {
    @NotBlank(message = "Email is required")
    private String residentEmail;
    @NotBlank(message = "Visitor's name is required")
    private String visitorName;
}
