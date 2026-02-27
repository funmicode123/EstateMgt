package com.funmicode.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OtpRequest {
    @NotBlank(message = "Email is required")
    private String residentEmail;
    @NotBlank(message = "Visitor's name is required")
    private String visitorName;
}
