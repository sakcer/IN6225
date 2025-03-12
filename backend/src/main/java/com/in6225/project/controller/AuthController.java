package com.in6225.project.controller;

import com.in6225.project.dto.LoginRequest;
import com.in6225.project.dto.ErrorDTO;
import com.in6225.project.dto.LoginResponseDTO;
import com.in6225.project.entity.User;
import com.in6225.project.exception.SystemErrorCode;
import com.in6225.project.security.JwtTokenProvider;
import com.in6225.project.security.UserDetailsServiceImpl;
import com.in6225.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmployeeId(), loginRequest.getPassword())
        );
        System.out.println(loginRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        User user = userService.getUserByEmployeeId(loginRequest.getEmployeeId());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwt, user);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(ErrorDTO.of(SystemErrorCode.SYSTEM_ERROR, ""));
    }
}
