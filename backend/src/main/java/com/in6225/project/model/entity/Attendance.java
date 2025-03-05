package com.in6225.project.model.entity;

import lombok.Data;
import lombok.Getter;

@Data
public class Attendance {
    private String id;
    private ClockType clockType;
    private String date;
    private String time;

    public enum ClockType {IN, OUT}
}