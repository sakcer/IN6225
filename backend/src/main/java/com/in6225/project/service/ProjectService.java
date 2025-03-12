package com.in6225.project.service;

import com.in6225.project.dto.ProjectDTO;
import com.in6225.project.entity.Project;
import com.in6225.project.entity.ProjectUser;
import com.in6225.project.entity.User;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Project addProject(Project project) {
//        Project project = new Project();
//        project.setId(projectDTO.getId());
//        project.setName(projectDTO.getName());
//        project.setLeader(userRepository.findById(projectDTO.getLeaderId()).orElse(null));
//        project.setDescription(projectDTO.getDescription());
//        project.setProgress(projectDTO.getProgress());
//        project.setStatus(projectDTO.getStatus());
//        project.setEndDate(projectDTO.getEndDate());
//        project.setStartDate(projectDTO.getStartDate());
//
//        List<ProjectUser> projectUsers = new ArrayList<>() {};
//        for (Long memberId : projectDTO.getMemberIds()) {
//            ProjectUser projectUser = new ProjectUser();
//            User user = userRepository.findById(memberId).orElse(null);
//            projectUser.setUser(user);
//            projectUser.setProject(project);
//            projectUsers.add(projectUser);
//        }
//        project.setProjectUsers(projectUsers);

        System.out.println(project);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Project updateProject(Project project) {
        if (projectRepository.existsById(project.getId())) {

//            Project project = new Project();
//            project.setId(projectDTO.getId());
//            project.setName(projectDTO.getName());
//            project.setLeader(userRepository.findById(projectDTO.getLeaderId()).orElse(null));
//            project.setDescription(projectDTO.getDescription());
//            project.setProgress(projectDTO.getProgress());
//            project.setStatus(projectDTO.getStatus());
//            project.setEndDate(projectDTO.getEndDate());
//            project.setStartDate(projectDTO.getStartDate());
//
//            List<ProjectUser> projectUsers = new ArrayList<>() {};
//            for (Long memberId : projectDTO.getMemberIds()) {
//                ProjectUser projectUser = new ProjectUser();
//                User user = userRepository.findById(memberId).orElse(null);
//                projectUser.setUser(user);
//                projectUsers.add(projectUser);
//            }
//            project.setProjectUsers(projectUsers);

            return projectRepository.save(project);
        } else {
            throw new EntityNotFoundException("User not found for id " + project.getId());
        }
    }
}
