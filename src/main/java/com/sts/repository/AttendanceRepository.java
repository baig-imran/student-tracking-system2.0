package com.sts.repository;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.dto.attendance.StudentAttendanceSummary;
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

	List<Attendance> findAllByStudent_StudentIdAndSemesterSubject_SubjectCode(String studentId, String subjectCode);
	
	List<Attendance> findAllBySemesterSubject_SubjectCodeAndStudent_StudentIdIn(String subjectCode, List<String> studentIds);

	List<Attendance> findAllByStudent_StudentId(String studentId);

	List<Attendance> findByStudent_StudentIdAndSemester_SemesterCode(String studentId, String semesterCode);
	
	@Query("""
		    SELECT new com.sts.dto.attendance.StudentAttendanceSummary(
		        a.student.studentId,
		        a.semester.semesterCode,
		        SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END),
		        COUNT(a),
		        (SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
		    )
		    FROM Attendance a
		    WHERE a.student.studentId IN :studentIds
		      AND :currentDate BETWEEN a.semester.startDate AND a.semester.endDate
		    GROUP BY a.student.studentId, a.semester.semesterCode
		    HAVING (SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a)) < :attendanceThreshold
		""")
		List<StudentAttendanceSummary> findStudentsWithLowAttendanceByActiveSemester(
		    @Param("studentIds") List<String> studentIds,
		    @Param("attendanceThreshold") double attendanceThreshold,
		    @Param("currentDate") LocalDate currentDate);
	
	@Query("""
		    SELECT new com.sts.dto.attendance.StudentAttendanceSummary(
		        a.student.studentId,
		        a.semester.semesterCode,
		        SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END),
		        COUNT(a),
		        (SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
		    )
		    FROM Attendance a
		    WHERE a.student.studentId IN :studentIds
		      AND a.semester.semesterSerialNumber = (
		          SELECT MAX(a2.semester.semesterSerialNumber)
		          FROM Attendance a2
		          WHERE a2.student.studentId = a.student.studentId
		      )
		    GROUP BY a.student.studentId, a.semester.semesterCode
		    HAVING (SUM(CASE WHEN a.isPresent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a)) < :attendanceThreshold
		""")
		List<StudentAttendanceSummary> findStudentsWithLowAttendanceInLatestSemester(
		    @Param("studentIds") List<String> studentIds,
		    @Param("attendanceThreshold") double attendanceThreshold);

	

	
	
}
