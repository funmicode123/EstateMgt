package com.funmicode.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExitPassRequest {
    @NotNull(message = "Resident email is required")
    private String residentEmail;
    @NotNull(message = "Visitor name is required")
    private String visitorName;

}
