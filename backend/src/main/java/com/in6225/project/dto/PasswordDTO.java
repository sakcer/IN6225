package com.in6225.project.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
