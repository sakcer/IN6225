package com.in6225.project.repository;

import com.in6225.project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmployeeId(String employeeId);

    @Query("SELECT u.department, COUNT(u) FROM User u GROUP BY u.department")
    List<Object[]> countByDepartment();

    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.joinDate) = :date")
    Long countByJoinDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(DISTINCT u.department) FROM User u")
    Long countDistinctDepartment();

    Long countByStatus(User.UserStatus status);
}
