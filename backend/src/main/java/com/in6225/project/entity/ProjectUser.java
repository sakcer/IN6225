package com.in6225.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project_user")
@Data
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
