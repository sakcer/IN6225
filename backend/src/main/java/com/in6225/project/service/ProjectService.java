package com.in6225.project.service;

import com.in6225.project.entity.Project;
import com.in6225.project.entity.User;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getAllProjectsByUserId(Long id) {
        return projectRepository.findByUserId(id);
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Project updateProject(Project project) {
        if (projectRepository.existsById(project.getId())) {
            return projectRepository.save(project);
        } else {
            throw new EntityNotFoundException("User not found for id " + project.getId());
        }
    }
}
