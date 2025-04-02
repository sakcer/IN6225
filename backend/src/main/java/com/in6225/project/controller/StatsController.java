package com.in6225.project.controller;

import com.in6225.project.model.dto.DepartmentDistDTO;
import com.in6225.project.model.dto.EmployeeTrendDTO;
import com.in6225.project.model.dto.StatsOverviewDTO;
import com.in6225.project.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;

    @GetMapping("/overview")
    public StatsOverviewDTO getStatsOverview() {
        return statsService.getStatsOveriew();
    }

    @GetMapping("/departments/distribution")
    public DepartmentDistDTO getDepartmentDist() {
        return statsService.getDepartmentDist();
    }

    @GetMapping("/employees/trend")
    public EmployeeTrendDTO getEmployeeTrend() {
        return statsService.getEmployeeTrend();
    }

}
