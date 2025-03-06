package com.in6225.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "attendance", indexes = @Index(name = "idx_user_id", columnList = "user_id"))
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private ClockType clockType;
    private LocalDate date;
    private LocalTime time;

    public enum ClockType {IN, OUT}

    @PrePersist
    public void generateDateAndTime() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
        if (this.time == null) {
            this.time = LocalTime.now();
        }
    }

}