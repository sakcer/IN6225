package com.in6225.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeTrendDTO {
    private List<String> dates;
    private List<Long> joinCounts;

//    private List<Trend> trends;
//
//    private static class Trend {
//        private Date date;
//        private Integer value;
//    }
}
