package com.in6225.project.service;

import com.in6225.project.model.dto.ProjectDTO;
import com.in6225.project.model.entity.Project;
import com.in6225.project.model.entity.User;
import com.in6225.project.model.mapper.ProjectMapper;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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


    public Map<String, Object> getProjectsByFilter(Integer page, Integer size, String sort, String query, Project.ProjectStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && "descending".equalsIgnoreCase(sortParams[1]))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);
        Sort sortOrder = Sort.by(order);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Specification<Project> spec = Specification.where(null);

        if (currentUser.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            if (currentUser.getAuthorities().stream()
                    .noneMatch(auth -> auth.getAuthority().equals("ROLE_LEADER"))) {
                spec = spec.and((root, criteriaQuery, cb) -> {
                    Join<Project, User> membersJoin = root.join("members", JoinType.LEFT);
                    return cb.equal(membersJoin.get("id"), currentUser.getUserId());
                });
            } else {
                spec = spec.and((root, criteriaQuery, cb) -> {
                    return cb.equal(root.get("leader").get("id"), currentUser.getUserId());
                });
            }
        }

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + query + "%"),
                            criteriaBuilder.like(root.get("description"), "%" + query + "%")
                    ));
        }

        if (status != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }


        Page<Project> projectPage = projectRepository.findAll(spec, pageable);

        List<ProjectDTO> projectDTOs = projectPage.stream()
                .map(projectMapper::toProjectDTO)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("total", projectPage.getTotalElements());
        result.put("projects", projectDTOs);
        return result;
    }
}
