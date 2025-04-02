package com.in6225.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DepartmentDistDTO {
    private List<Map<String, Object>> departments;

//    private static class DepartmentDTO {
//        User.Department department;
//        Integer value;
//    }
}
