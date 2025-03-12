package com.in6225.project.entity;

import com.fasterxml.jackson.annotation.*;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.service.UserService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private ProjectStatus status;
    private int progress;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private Long leaderId;
    private Set<Long> memberIds;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "leader_id", nullable = false)
//    @JsonIgnore
//    private User leader;
//
//    @OneToMany(mappedBy = "project")
//    @JsonIgnore
//    private List<ProjectUser> projectUsers = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "project_members",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
//    @JsonIgnore
//    @JsonIgnoreProperties("members")
//    private Set<User> members;

    public enum ProjectStatus {
        ACTIVE, COMPLETED
    }

//    @JsonSetter("leaderId")
//    public void setLeaderId(Long leaderId) {
//        this.leader = new User();
//        leader.setId(leaderId);
//    }
//
//    @JsonSetter("projectIds")
//    public void setLeaderId(Long leaderId) {
//        this.leader = new User();
//        this.leader.setId(leaderId);
//    }
//    public Project(
//            @JsonProperty("leaderId") Long leaderId,
//            @JsonProperty("memberIds") Set<Long> memberIds) {
//
//        this.leader = new User();
//        this.leader.setId(leaderId);
//
//        this.projectUsers = new ArrayList<>() {};
//        for (Long memberId : memberIds) {
//            ProjectUser projectUser = new ProjectUser();
//            User user = new User();
//            user.setId(memberId);
//            projectUser.setUser(user);
//            this.projectUsers.add(projectUser);
//        }
//    }

//    public Project() {
//    }

//    @JsonGetter("leaderId")
//    public Long getLeaderId() {
//        return this.leader.getId();
//    }
//
//    @JsonGetter("memberIds")
//    public List<Long> getMemberIds() {
//        return this.projectUsers.stream()
//                .map(projectUser -> projectUser.getProject().getId())
//                .collect(Collectors.toList());
//    }

}


