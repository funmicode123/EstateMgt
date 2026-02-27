package com.funmicode.data.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Apartment {
    @Id
    private String id;
    @NotBlank(message = "House number is required")
    private String houseNo;
    @NotBlank(message = "Specific block is required")
    private String block;
    @NotBlank(message = "Street name is required")
    private String streetName;

}
