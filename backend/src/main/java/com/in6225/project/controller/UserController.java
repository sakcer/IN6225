package com.in6225.project.controller;

import com.in6225.project.model.dto.MsgDTO;
import com.in6225.project.model.dto.PwdUpdateDTO;
import com.in6225.project.model.dto.UserBasicDTO;
import com.in6225.project.model.dto.UserDetailsDTO;
import com.in6225.project.security.CustomUserDetails;
import com.in6225.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employee")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userService.getUserById(currentUser.getUserId()));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<?> userResponseDTOSs = userService.getAllUsers();
        return ResponseEntity.ok(userResponseDTOSs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserBasicDTO userRequestDTO) {
        userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(new MsgDTO("User updated: " + id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDetailsDTO userRequestDTO) {
        userService.addUser(userRequestDTO);
        return ResponseEntity.ok(new MsgDTO(userRequestDTO.getName() + ", add"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MsgDTO("User deleted: " + id));
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.userId")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @Valid @RequestBody PwdUpdateDTO passwordUpdateDTO) {
        userService.updatePassword(id, passwordUpdateDTO);
        return ResponseEntity.ok(new MsgDTO("Password updated"));
    }
}
