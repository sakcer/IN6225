package com.in6225.project.service;

import com.in6225.project.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public Optional<User> getUserById(String id) {
        User user = new User();
        return Optional.of(user);
    }

    public Optional<List<User>> getAllUsers() {
        // 假设从数据库获取所有员工信息
        List<User> employees = new ArrayList<>();

        User employee1 = new User();
        User employee2 = new User();

        employees.add(employee1);
        employees.add(employee2);

        return Optional.of(employees);
    }
}
