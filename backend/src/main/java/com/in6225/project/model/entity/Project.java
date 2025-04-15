package com.in6225.project.model.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private int progress;
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @JsonIgnoreProperties({"projects"})
    private User leader;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"projects"})
    private List<User> members = new ArrayList<>();

    public enum ProjectStatus {
        ACTIVE, COMPLETED
    }

    @PrePersist
    public void init() {
        if (this.status == null) {
            this.status = ProjectStatus.ACTIVE;
        }
        this.startDate = new Date();
    }
}
