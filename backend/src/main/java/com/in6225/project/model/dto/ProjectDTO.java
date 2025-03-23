package com.in6225.project.model.dto;

import com.in6225.project.model.entity.Project;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Project status is required")
    private Project.ProjectStatus status;

    @Min(value = 0, message = "Progress cannot be negative")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private int progress;

    private Date startDate;

    private Date endDate;

    @NotNull(message = "Project leader is required")
    private UserBasicDTO leader;

    private List<UserBasicDTO> members;
}

