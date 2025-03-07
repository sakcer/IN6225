package com.in6225.project.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String employeeId;
    private String password;
}
