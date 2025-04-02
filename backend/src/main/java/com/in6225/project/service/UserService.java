package com.in6225.project.service;

import com.in6225.project.model.entity.Project;
import com.in6225.project.model.entity.User;
import com.in6225.project.model.mapper.UserMapper;
import com.in6225.project.model.dto.PwdUpdateDTO;
import com.in6225.project.model.dto.UserBasicDTO;
import com.in6225.project.model.dto.UserDetailsDTO;
import com.in6225.project.repository.ProjectRepository;
import com.in6225.project.repository.UserRepository;
import com.in6225.project.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private UserMapper userMapper;

    public Object getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for " + id));
        return userMapper.toUserDetailsDTO(user);
    }

    public User getUserByEmployeeId(String EID) {
        User user = userRepository.findByEmployeeId(EID);
        if (user == null) {
            throw new EntityNotFoundException("User not found for employeeId: " + EID);
        }
        return user;  // 将 User 转换为 UserResponseDTO
    }

    public Map<String, Object> getEmployeesByFilter(Integer page, Integer size, String sort, String query, User.UserStatus status) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = (sortParams.length > 1 && "descending".equalsIgnoreCase(sortParams[1]))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);
        Sort sortOrder = Sort.by(order);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Specification<User> spec = Specification.where(null);

        if (query != null && !query.isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + query + "%"),
                            criteriaBuilder.like(root.get("email"), "%" + query + "%"),
                            criteriaBuilder.like(root.get("employeeId"), "%" + query + "%")
                    ));
        }

        if (status != null) {
            spec = spec.and((root, criterialQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<UserDetailsDTO> userDetailsDTOS = userPage.stream()
                .map(userMapper::toUserDetailsDTO)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("total", userPage.getTotalElements());
        result.put("employees", userDetailsDTOS);
        return result;
    }

    public List<Object> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        List<User> users = userRepository.findAll();

        if (currentUser.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return users.stream()
                    .map(userMapper::toUserBasicDTO)
                    .collect(Collectors.toList());  // 将 List<User> 转换为 List<UserResponseDTO>
        }
        return users.stream()
                .map(userMapper::toUserDetailsDTO)
                .collect(Collectors.toList());  // 将 List<User> 转换为 List<UserResponseDTO>
    }

    public void addUser(UserDetailsDTO userRequestDTO) {
        User user = new User();
        userMapper.toUser(user, userRequestDTO);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found for id: " + id);
        }

        List<Project> projects = projectRepository.findByUserId(id);
        if (!projects.isEmpty()) {
            String projectIds = projects.stream()
                    .map(Project::getName)  // 转换为ID字符串
                    .limit(2)  // 只取前两个项目
                    .collect(Collectors.joining(", "));  // 使用逗号分隔

            int totalProjects = projects.size();  // 计算总共关联的项目数量
            String message = "Can't delete, associated with projects: [" + projectIds;

            if (totalProjects > 2) {
                message += ", ... (" + totalProjects + " projects total)";  // 超过两个时显示总数
            } else {
                message += "]";
            }
            throw new SecurityException(message);
        }

        userRepository.deleteById(id);
    }

    public void updateUser(Long id, UserBasicDTO userRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found for id: " + id);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found for id: " + id));

        System.out.println(currentUser.getAuthorities());

        if (userRequestDTO instanceof UserDetailsDTO) {
            if (currentUser.getAuthorities().stream()
                    .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Only an admin can update details");
            }
            userMapper.toUser(user, (UserDetailsDTO) userRequestDTO);
        } else {
            if (!currentUser.getUserId().equals(id) && currentUser.getAuthorities().stream()
                    .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Only the user themselves or an admin can update profile");
            }
            userMapper.toUser(user, userRequestDTO);
        }

        userRepository.save(user);
    }

    public void updatePassword(Long id, PwdUpdateDTO passwordUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + id));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(passwordUpdateDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getCurrentPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the current password");
        }

        if (!passwordUpdateDTO.getNewPassword().equals(passwordUpdateDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        String encodedNewPassword = passwordEncoder.encode(passwordUpdateDTO.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

}
