package com.in6225.project.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PwdUpdateDTO {
    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
}
