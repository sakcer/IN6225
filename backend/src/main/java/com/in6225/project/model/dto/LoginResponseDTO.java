package com.in6225.project.model.dto;

import com.in6225.project.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDetailsDTO user;
    private User.UserRole role;
}
