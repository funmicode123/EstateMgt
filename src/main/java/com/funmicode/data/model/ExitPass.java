package com.funmicode.data.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "ExitPass")
public class ExitPass {

    @Id
    private String id;
    @NotNull(message = "Visitor name is required")
    private String visitorName;
    @NotNull(message = "Resident email is required")
    private String residentEmail;
    @NotBlank(message = "Time is required")
    private LocalDateTime exitTime;

}

