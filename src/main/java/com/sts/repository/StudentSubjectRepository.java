package com.sts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Student;
import com.sts.entity.StudentSubject;

@Repository
public interface StudentSubjectRepository extends JpaRepository<StudentSubject, Long> {
	
	 @Query("SELECT ss FROM StudentSubject ss "
	 		+ "WHERE ss.semesterSubject.subjectCode = :subjectCode "
	 		+ "AND ss.student.studentId =:studentId")
	    Optional<StudentSubject> getBySubjectCodeAndStudentId(@Param("subjectCode") String subjectCode, @Param("studentId") String studentId);

	 List<StudentSubject> findBySemesterSubject_SubjectCode(String subjectCode);

		boolean existsBySemesterSubject_SubjectCodeAndStudent_StudentId(String subjectCode, String studentId);

	 
}