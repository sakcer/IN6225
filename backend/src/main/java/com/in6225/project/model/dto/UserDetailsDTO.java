package com.in6225.project.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.in6225.project.model.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailsDTO extends UserBasicDTO {
    @NotEmpty(message = "Employee ID is required")
    private String employeeId;

    @NotNull(message = "Role is required")  // 使用 @NotNull 而不是 @NotEmpty
    private User.UserRole role;

    @NotNull(message = "Status is required")  // 使用 @NotNull 而不是 @NotEmpty
    private User.UserStatus status;

    @NotNull(message = "Department is required")  // 使用 @NotNull 而不是 @NotEmpty
    private User.Department department;

    private Date joinDate;
}
