package com.in6225.project.service;

import com.in6225.project.entity.User;
import com.in6225.project.repository.RoleRepository;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Optional<User> getUserById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        user.getRoles().add(roleRepository.getByRole("EMPLOYEE"));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found for id " + user.getId());
        }
    }
}
