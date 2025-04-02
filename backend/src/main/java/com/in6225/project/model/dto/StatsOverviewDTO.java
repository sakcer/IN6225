package com.in6225.project.model.dto;

import lombok.Data;

@Data
public class StatsOverviewDTO {
    private Long totalEmployees;
    private Long newEmployees;
    private Long totalDepartments;
    private Long totalProjects;
    private Long activeEmployees;
    private Long completedProjects;
}

