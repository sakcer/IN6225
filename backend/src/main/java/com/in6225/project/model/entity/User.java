package com.in6225.project.model.entity;

import ch.qos.logback.core.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Entity
@Table(name = "users")
@Data
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Column(name = "employee_id", unique = true)
    private String employeeId;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String title;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Department department;
    private Date joinDate;

    @Getter
    public enum UserStatus {
        ACTIVE, INACTIVE
    }

    @Getter
    public enum UserRole {
        LEADER, ADMIN, USER
    }

    @Getter
    public enum Department {
        TCH, HR, SLS, MKT, FIN, LGL, OPS, CS,
    }

    @ManyToMany(mappedBy = "members")  // 由 Project 端维护
    @JsonIgnore
    private List<Project> projects = new ArrayList<>();

    @PrePersist
    public void init() {
        if (StringUtil.isNullOrEmpty(this.password)) {
            this.password = new BCryptPasswordEncoder().encode(this.employeeId);
        }
        this.joinDate = new Date();
    }

}

