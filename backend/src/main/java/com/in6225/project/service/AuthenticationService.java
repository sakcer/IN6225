package com.in6225.project.service;

import com.in6225.project.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
//    @Autowired
//    private UserRepository userRepository; // 用于查询用户的仓库

    public String login(String id, String password) {
        Optional<User> user = Optional.of(new User());

        if (!user.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return "";
    }

    public void logout(String token) {
    }

}
