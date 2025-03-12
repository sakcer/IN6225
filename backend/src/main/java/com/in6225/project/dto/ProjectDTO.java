package com.in6225.project.dto;

import com.in6225.project.entity.Project;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ProjectDTO {
    private Long id;
    private Long leaderId;
    private String name;
    private String description;
    private Project.ProjectStatus status;
    private int progress;
    private Date startDate;
    private Date endDate;
    private Set<Long> memberIds;
}
