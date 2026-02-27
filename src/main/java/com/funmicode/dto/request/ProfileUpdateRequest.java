package com.funmicode.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String gateLocation;
    private ApartmentRequest apartment;
}
