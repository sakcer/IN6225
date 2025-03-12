package com.in6225.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.*;

@Entity
@Table(name = "users")
@Data
//@DynamicUpdate
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

    private String position;
    private int salary;


   @PrePersist
    public void init() {
        if (this.employeeId == null || this.employeeId.isEmpty()) {
            this.employeeId = "EID_" + UUID.randomUUID().toString().substring(0, 5);  // 生成以"E"开头的ID
        }
        if (this.password == null || this.password.isEmpty()) {
            this.password = "123456";
        }
        if (this.salary == 0) {
            this.salary = 5000;
        }
        if (this.status == null) {
            this.status = UserStatus.ACTIVE;
        }
        if (this.role == null) {
            this.role = UserRole.USER;
        }
        this.joinDate = new Date();
    }

    @Getter
    public enum UserStatus {
        ACTIVE("ACTIVE"),
        INACTIVE("INACTIVE");

        private final String userStatusName;

        UserStatus(String userStatusName) {
            this.userStatusName = userStatusName;
        }
    }


    @Getter
    public enum UserRole {
        OWNER("OWNER"),
        ADMIN("ADMIN"),
        USER("USER");

        private final String userRoleName;
        UserRole(String userRoleName) {
            this.userRoleName = userRoleName;
        }
    }

    @Getter
    public enum Department {
        TCH("TCH"),      // 技术
        HR("HR"),        // 人力资源
        SLS("SLS"),      // 销售
        MKT("MKT"),      // 市场
        FIN("FIN"),      // 财务
        LGL("LGL"),      // 法务
        OPS("OPS"),      // 运维
        CS("CS");        // 客服

        private final String departmentName;

        Department(String departmentName) {
            this.departmentName = departmentName;
        }
    }


}

