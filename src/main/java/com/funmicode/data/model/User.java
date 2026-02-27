package com.funmicode.data.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "User")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phoneNumber;

    @NotNull(message = "User role is required")
    private UserRole role;

    private AccountStatus status = AccountStatus.PENDING; // Default for Residents

    private String gateLocation; // Only for Security

    @org.springframework.data.mongodb.core.mapping.DBRef
    private Apartment apartment; // Only for Residents

    private String otp;
    private java.time.LocalDateTime otpExpiry;

    private List<String> visitorLogs = new ArrayList<>();
}
