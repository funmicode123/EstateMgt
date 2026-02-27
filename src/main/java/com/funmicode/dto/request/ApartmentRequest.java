package com.funmicode.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApartmentRequest {
    @NotBlank(message = "House number is required")
    private String houseNo;
    @NotBlank(message = "Block is required")
    private String block;
    @NotBlank(message = "Street name is required")
    private String streetName;
}
