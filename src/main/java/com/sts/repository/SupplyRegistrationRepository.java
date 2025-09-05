package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Exam;
import com.sts.entity.Student;
import com.sts.entity.SupplyRegistration;

@Repository
public interface SupplyRegistrationRepository extends JpaRepository<SupplyRegistration, Long> {
	
	@Query("SELECT sr.student.studentId FROM SupplyRegistration sr WHERE sr.exam.examCode = :examCode")
	List<String> findStudentIdsByExamCode(@Param("examCode") String examCode);

	boolean existsByExamAndStudent(Exam exam, Student student);

	boolean existsByExam_ExamCodeAndStudent_StudentId(String examCode, String studentId);
	

}
