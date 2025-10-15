package com.corporate.solutions.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min = 3, max = 100)
    private String username;

    @NotBlank @Size(min = 6, max = 200)
    private String password;
}
