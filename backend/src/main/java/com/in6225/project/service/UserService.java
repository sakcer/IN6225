package com.in6225.project.service;

import com.in6225.project.model.entity.User;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    public Optional<User> getUserById(String id) {
        User user = generateRandomUser(id);
        return Optional.of(user);
    }

    public Optional<List<User>> getAllUsers() {
        // 假设从数据库获取所有员工信息
        List<User> employees = new ArrayList<>();


        int len = new Random().nextInt(10);
        for (int i = 0; i < len; i++) {
            employees.add(generateRandomUser(Integer.toString(i)));
        }

        return Optional.of(employees);
    }

    public Optional<User> addUser(User user) {
        user.setId("E" + new Random().nextInt(9999));
        return Optional.of(user);
    }

    public void deleteUser(String id) {
    }

    public void updateUser(User user) {

    }

    // 随机生成用户的函数
    private User generateRandomUser(String id) {
        Random random = new Random();
        User user = new User();

        // 随机生成数据
        user.setId(id);  // 使用传入的ID
        user.setName("User" + random.nextInt(1000));  // 随机生成姓名
        user.setDepartment("Department" + random.nextInt(10));  // 随机生成部门
        user.setPosition("Position" + random.nextInt(5));  // 随机生成职位
        user.setEmail("user" + random.nextInt(1000) + "@example.com");  // 随机生成邮箱
        user.setSalary(random.nextInt(10000) + 3000);  // 随机生成工资（3000到13000之间）
        user.setRole(random.nextBoolean() ? "admin" : "normal");  // 随机生成角色
        user.setStatus(random.nextBoolean() ? "active" : "inactive");  // 随机生成状态

        return user;
    }
}
