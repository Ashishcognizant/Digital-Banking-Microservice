package com.cts.user.dto;

import com.cts.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    @Email
    private String email;
    
    private String phone;
    
    @NotNull
    private Role role;
}
