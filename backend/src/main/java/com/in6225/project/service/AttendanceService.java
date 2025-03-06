package com.in6225.project.service;

import com.in6225.project.entity.Attendance;
import com.in6225.project.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance clockInOut( Attendance request) {
        return attendanceRepository.save(request);
    }

    public List<Attendance> getAttendanceRecords(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }
}
