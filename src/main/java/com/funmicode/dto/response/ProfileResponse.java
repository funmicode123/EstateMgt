package com.funmicode.dto.response;

import com.funmicode.data.model.Apartment;
import lombok.Data;

@Data
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String gateLocation;
    private Apartment apartment;
    private boolean success;
    private String message;
}
