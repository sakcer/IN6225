package com.in6225.project.controller;

import com.in6225.project.entity.Project;
import com.in6225.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<Project> geProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();

        Map<String, Object> response = new HashMap<>();
        response.put("projects", projects);
        response.put("total", projects.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {

        System.out.println(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addProject(project));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete " + id);
        return ResponseEntity.ok(response);
    }


    @PutMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(project));
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllProjectsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getAllProjectsByUserId(id));
    }
}