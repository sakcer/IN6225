package com.in6225.project.service;

import com.in6225.project.model.dto.LoginRequestDTO;
import com.in6225.project.model.dto.LoginResponseDTO;
import com.in6225.project.model.dto.MsgDTO;
import com.in6225.project.model.entity.User;
import com.in6225.project.model.mapper.UserMapper;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.UserDetailsServiceImpl;
import com.in6225.project.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository; // 用于查询用户的仓库

    @Autowired
    private UserMapper userMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmployeeId(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // String jwt = jwtTokenProvider.generateToken(authentication);

        // generate Access Token & Refresh Token
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        User user = userRepository.findByEmployeeId(loginRequest.getEmployeeId());

        return new LoginResponseDTO(accessToken, refreshToken, userMapper.toUserDetailsDTO(user), user.getRole());
    }

    public MsgDTO refreshAccessToken(String refreshToken) {
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtTokenProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        return new MsgDTO(newAccessToken);
    }

     public void logout() {
    }

}
