package com.in6225.project.controller;

import com.in6225.project.dto.PasswordDTO;
import com.in6225.project.entity.User;
import com.in6225.project.security.CustomUserDetails;
import com.in6225.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        Long userId = currentUser.getUserId();
        Optional<User> user = userService.getUserById(userId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/all")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        System.out.println("XXXXXXXXX");
        List<User> users = userService.getAllUsers();
        System.out.println("XXXXXXXXX");

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("total", users.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete " + id);
        return ResponseEntity.ok(response);
    }


    @PutMapping
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PostMapping("password/{id}")
    public ResponseEntity<User> updateUserPassword(@PathVariable Long id, @RequestBody PasswordDTO passwordDTO) {
        return ResponseEntity.ok(userService.updatePassword(id, passwordDTO));
    }
}
