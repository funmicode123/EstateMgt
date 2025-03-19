package com.funmicode.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExitPassRequest {
    @NotNull(message = "Resident email is required")
    private String residentEmail;
    @NotNull(message = "Visitor name is required")
    private String visitorName;

}
