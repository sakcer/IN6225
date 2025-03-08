package com.in6225.project.entity;

import com.in6225.project.repository.RoleRepository;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
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
    private String password;

    private String name;
    private String position;
    private String email;
    private int salary;
    private String status;

    @ManyToMany(fetch = FetchType.EAGER) // 一般来说会使用 EAGER 以便加载权限
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;  // 关联员工

    @PrePersist
    public void init() {
        if (this.employeeId == null || this.employeeId.isEmpty()) {
            this.employeeId = "E" + UUID.randomUUID().toString().substring(0, 5);  // 生成以"E"开头的ID
        }
        if (this.password == null || this.password.isEmpty()) {
            this.password = "123456";
        }
        if (this.salary == 0) {
            this.salary = 5000;
        }
    }
}
