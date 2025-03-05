package com.in6225.project.service;

import com.in6225.project.model.entity.Attendance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    public void clockInOut( Attendance request) {
    }

    public Map<String, Object> getAttendanceRecords(String id) {
        List<Attendance> attendanceRecords = new ArrayList<>();

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("records", attendanceRecords);

        return response;
    }
}
