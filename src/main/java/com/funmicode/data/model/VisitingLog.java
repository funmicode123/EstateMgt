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
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String securityGuardId; // ID of the security guard who checked them in

    private String apartmentDetails; // Snapshot of apartment details for quick reference
    private OtpStatus otpStatus;
}