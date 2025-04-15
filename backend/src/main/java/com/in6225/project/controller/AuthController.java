package com.in6225.project.controller;

import com.in6225.project.model.dto.LoginRequestDTO;
import com.in6225.project.model.dto.LoginResponseDTO;
import com.in6225.project.model.dto.MsgDTO;
import com.in6225.project.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        LoginResponseDTO lg = authService.login(loginRequest, response);
        return ResponseEntity.ok(lg);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return ResponseEntity.ok(authService.refreshAccessToken(cookie.getValue()));
                }
            }
        }
        throw new AuthenticationException("Refresh token is missing") {};
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        String cookieString = "refreshToken=; Path=/api/v1/auth; HttpOnly; Max-Age=0";
        response.setHeader("Set-Cookie", cookieString);

        return ResponseEntity.ok(new MsgDTO("Logout successfully"));
    }
}
