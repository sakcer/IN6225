package com.in6225.project.model.entity;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String department;
    private String position;
    private String email;
    private int salary;
    private String role;
    private String status;
}
