package com.in6225.project.entity;

import com.fasterxml.jackson.annotation.*;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.service.UserService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
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
    @Temporal(TemporalType.DATE)
    private Date endDate;

//    private Long leaderId;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    @JsonIgnoreProperties({"email", "password", "role", "status", "title", "avatar", "department", "joinDate", "position", "salary", "projects"})
    private User leader;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"email", "password", "role", "status", "title", "avatar", "department", "joinDate", "position", "salary", "projects"})
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


