package com.funmicode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidResponse {
    private String message;
    private boolean success;
    private String apartmentDetails;
}
