package com.in6225.project.controller;

import com.in6225.project.model.dto.MsgDTO;
import com.in6225.project.model.dto.ProjectDTO;
import com.in6225.project.model.entity.Project;
import com.in6225.project.security.CustomUserDetails;
import com.in6225.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

//    @GetMapping("/{id}")
//    public ResponseEntity<?> geProjectById(@PathVariable Long id) {
//        return ResponseEntity.ok(projectService.getProjectById(id));
//    }

    @GetMapping("/all/me")
    public ResponseEntity<?> getAllProjectsByUserId(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(projectService.getAllProjectsByUserId(currentUser.getUserId()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllProjects() {
        List<ProjectDTO> projectDTOs = projectService.getAllProjects();
        return ResponseEntity.ok(projectDTOs);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        projectService.addProject(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MsgDTO("Add Project Successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new MsgDTO("Delete " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_LEADER')")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(new MsgDTO("Update " + id));
    }

}