package com.funmicode.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "blacklisted_visitors")
@Getter
@Setter
public class BlacklistedVisitor {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String phoneNumber;
    private String reason;
    private String addedBy; // Admin email
    private LocalDateTime addedAt;
}
