package com.in6225.project.controller;

import com.in6225.project.model.dto.LoginRequestDTO;
import com.in6225.project.model.dto.MsgDTO;
import com.in6225.project.security.jwt.JwtTokenProvider;
import com.in6225.project.security.UserDetailsServiceImpl;
import com.in6225.project.service.AuthService;
import com.in6225.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new MsgDTO("Logout successfully"));

    }
}
