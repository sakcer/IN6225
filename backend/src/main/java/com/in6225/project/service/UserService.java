package com.in6225.project.service;

import com.in6225.project.dto.PasswordDTO;
import com.in6225.project.entity.User;
import com.in6225.project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByEmployeeId(String EID) {
        return userRepository.findByEmployeeId(EID);
    }

    public List<User> getAllUsers() {
        try {
            List<User> user = userRepository.findAll();
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User addUser(User user) {
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

    public User updatePassword(Long id, PasswordDTO passwordDTO) {
        User user = userRepository.findById(id).orElse(null);
        if (!passwordDTO.getCurrentPassword().equals(user.getPassword())) {
            throw new EntityNotFoundException("Password Incorrect");
        }
        if (passwordDTO.getNewPassword().equals(passwordDTO.getCurrentPassword())) {
            throw new EntityNotFoundException("Password cannot be same");
        }
        if(passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())){
            if (userRepository.existsById(id)) {
                user.setPassword(passwordDTO.getNewPassword());
                return userRepository.save(user);
            } else {
                throw new EntityNotFoundException("User not found for id ");
            }
        }
        throw new EntityNotFoundException("ERROR Password");
    }
}
