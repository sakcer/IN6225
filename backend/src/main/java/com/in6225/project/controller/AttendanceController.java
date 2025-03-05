package com.in6225.project.controller;

import com.in6225.project.model.entity.Attendance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @PostMapping("/clock")
    public ResponseEntity<Map<String, String>> clockInOut(@RequestBody Attendance request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Clock " + request.getClockType() + " successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<Map<String, Object>> getAttendanceRecords(@PathVariable String id) {
        List<Map<String, String>> attendanceRecords = new ArrayList<>();

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("attendanceRecords", attendanceRecords);

        return ResponseEntity.ok(response);}
}
