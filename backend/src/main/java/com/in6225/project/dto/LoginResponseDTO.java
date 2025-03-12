package com.in6225.project.dto;

import com.in6225.project.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private User user;
    private User.UserRole role;

    public LoginResponseDTO(String token, User user) {
        this.token = token;
        this.user = user;
        this.role = user.getRole();
    }
}
