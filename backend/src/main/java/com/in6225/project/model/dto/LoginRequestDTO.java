package com.in6225.project.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotEmpty(message = "EmployeeId is required")
    private String employeeId;

    @NotEmpty(message = "Password is required")
    private String password;
}
