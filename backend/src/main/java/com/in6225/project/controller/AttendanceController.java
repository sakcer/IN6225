package com.in6225.project.controller;

import com.in6225.project.entity.Attendance;
import com.in6225.project.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/clock")
    public ResponseEntity<Map<String, String>> clockInOut(@RequestBody Attendance request) {
        Attendance attendance = attendanceService.clockInOut(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Clock " + attendance.getClockType() + " successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/records/{userId}")
    public ResponseEntity<Map<String, Object>> getAttendanceRecords(@PathVariable Long userId) {
        List<Attendance> attendanceRecords = attendanceService.getAttendanceRecords(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("attendanceRecords", attendanceRecords);

        return ResponseEntity.ok(response);
    }

}
