package com.sts.repository;


import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Attendance;

import jakarta.transaction.Transactional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {

	@Modifying
    @Transactional
    @Query(value = "UPDATE student_tracking_system.attendance SET is_present = :isPresent " +
                   "WHERE student_id = :studentId AND subject_code = :subjectCode " +
                   "AND attendance_date = :attendanceDate AND period = :period", 
           nativeQuery = true)
    int updateAttendance(
            @Param("isPresent") Boolean isPresent,
            @Param("studentId") String studentId,
            @Param("subjectCode") String subjectCode,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("period") Integer period
    );
}
