package com.funmicode.dto.request;

import com.funmicode.data.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSignupRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max =6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "RESIDENT|ADMIN|SECURITY", message = "Role must be either 'RESIDENT' or 'ADMIN' or 'SECURITY' ")
    private String role;
}
