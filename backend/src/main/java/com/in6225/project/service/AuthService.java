package com.in6225.project.service;

import com.in6225.project.model.dto.LoginRequestDTO;
import com.in6225.project.model.dto.LoginResponseDTO;
import com.in6225.project.model.entity.User;
import com.in6225.project.model.mapper.UserMapper;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository; // 用于查询用户的仓库

    @Autowired
    private UserMapper userMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmployeeId(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmployeeId(loginRequest.getEmployeeId());

        return new LoginResponseDTO(jwt, userMapper.toUserDetailsDTO(user), user.getRole());
    }

     public void logout() {
    }

}
