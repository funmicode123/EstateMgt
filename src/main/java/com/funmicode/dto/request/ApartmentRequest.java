package com.funmicode.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentRequest {
    @NotBlank(message = "Block is required")
    private String block;
    @NotBlank(message = "street name is required")
    private String streetName;
}
