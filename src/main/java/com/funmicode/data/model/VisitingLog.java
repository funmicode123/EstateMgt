package com.funmicode.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
public class VisitingLog {

    @Id
    private String id;
    private String residentEmail;
    private String visitorName;
    private String otp;
    private LocalDateTime otpCreatedTime;
    private LocalDateTime otpExpiredTime;
    private OtpStatus otpStatus;


}
