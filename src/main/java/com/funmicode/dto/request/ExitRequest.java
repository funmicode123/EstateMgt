package com.funmicode.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExitRequest {
    @NotNull(message = "Visitor name is required")
    private String visitorName;
}
