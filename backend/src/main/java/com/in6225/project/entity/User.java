package com.in6225.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_id", unique = true)
    private String employeeId;
    private String name;
    private String department;
    private String position;
    private String password;
    private String email;
    private int salary;
    private String role;
    private String status;

    @PrePersist
    public void generateEmployeeId() {
        if (this.employeeId == null || this.employeeId.isEmpty()) {
            this.employeeId = "E" + UUID.randomUUID().toString().substring(0, 5);  // 生成以"E"开头的ID
        }
    }
}
