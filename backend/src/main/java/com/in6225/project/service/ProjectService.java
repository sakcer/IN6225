package com.in6225.project.service;

import com.in6225.project.model.dto.ProjectDTO;
import com.in6225.project.model.entity.Project;
import com.in6225.project.model.mapper.ProjectMapper;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMapper projectMapper;

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found for id " + id));
        return projectMapper.toProjectDTO(project);
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toProjectDTOs(projects);
    }

    public List<ProjectDTO> getAllProjectsByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found for id " + id);
        }
        List<Project> projects = projectRepository.findByUserId(id);
        return projectMapper.toProjectDTOs(projects);
    }

    public void addProject(ProjectDTO projectDTO) {
        Project project = new Project();
        projectMapper.toProject(project, projectDTO);
        projectMapper.updateLeader(project, projectDTO);
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found for id " + id);
        }
        projectRepository.deleteById(id);
    }

    public void updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found for id: " + id));
        projectMapper.toProject(project, projectDTO);
        projectMapper.updateLeader(project, projectDTO);
        projectRepository.save(project);
    }

}
