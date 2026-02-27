package com.funmicode.dto.request;

import lombok.Data;

@Data
public class BlacklistRequest {
    private String name;
    private String phoneNumber;
    private String reason;
}
