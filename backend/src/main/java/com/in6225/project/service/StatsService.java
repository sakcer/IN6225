package com.in6225.project.service;

import com.in6225.project.model.dto.DepartmentDistDTO;
import com.in6225.project.model.dto.EmployeeTrendDTO;
import com.in6225.project.model.dto.StatsOverviewDTO;
import com.in6225.project.model.entity.Project;
import com.in6225.project.model.entity.User;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StatsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public StatsOverviewDTO getStatsOveriew() {
        StatsOverviewDTO statsSummaryDTO = new StatsOverviewDTO();
        statsSummaryDTO.setTotalEmployees(userRepository.count());
        statsSummaryDTO.setNewEmployees(userRepository.countByJoinDate(LocalDate.now()));
        statsSummaryDTO.setTotalDepartments(userRepository.countDistinctDepartment());
        statsSummaryDTO.setTotalProjects(projectRepository.count());
        statsSummaryDTO.setActiveEmployees(userRepository.countByStatus(User.UserStatus.ACTIVE));
        statsSummaryDTO.setCompletedProjects(projectRepository.countByStatus(Project.ProjectStatus.ACTIVE));

        return statsSummaryDTO;
    }

    public DepartmentDistDTO getDepartmentDist() {
        List<Object[]> results = userRepository.countByDepartment();

        List<Map<String, Object>> departmentData = results.stream()
                .map(result -> Map.of("name", ((User.Department) result[0]).name(), "value", result[1]))
                .collect(Collectors.toList());

        return new DepartmentDistDTO(departmentData);
    }

    public EmployeeTrendDTO getEmployeeTrend() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

        List<LocalDate> dates = IntStream.rangeClosed(-6, 0)
                .mapToObj(LocalDate.now()::plusDays)
                .toList();

        List<Long> counts = dates.stream()
                .map(date -> userRepository.countByJoinDate(date))
                .toList();

        return new EmployeeTrendDTO(
                dates.stream().map(formatter::format).toList(),
                counts);
    }
}
