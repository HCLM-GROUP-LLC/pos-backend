package com.hclm.web.repository;

import com.hclm.web.entity.EmployeesAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 员工考勤存储库
 *
 * @author hanhua
 * @since 2025/10/10
 */
@Repository
public interface EmployeesAttendanceRepository extends JpaRepository<EmployeesAttendance, Long> {
}
