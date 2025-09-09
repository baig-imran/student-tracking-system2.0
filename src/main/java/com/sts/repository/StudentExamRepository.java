package com.sts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Exam;
import com.sts.entity.Student;
import com.sts.entity.StudentExam;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long>, JpaSpecificationExecutor<StudentExam> {

	boolean existsByExamAndStudent(Exam exam, Student student);
	List<StudentExam> getByExamCode(String examCode);

	boolean existsByStudent_StudentIdAndExamCode(String firstStudentId, String examCode);
	List<StudentExam> findByStudent_StudentId(String studentId);
	List<StudentExam> findByExam_SemesterSubject_SubjectCodeAndExam_ExamType(String subjectCode, String string);
	List<StudentExam> findByExam_ExamCode(String examCode);
	
	@Query("SELECT se FROM StudentExam se " +
		       "WHERE se.student.studentId = :studentId " +
		       "AND se.exam.semesterSubject.subjectCode = :subjectCode " +
		       "AND se.exam.examSubType = :examSubType " +
		       "ORDER BY se.exam.examDate DESC")
		Optional<StudentExam> findLatestByStudentIdAndSubjectCodeAndExamSubType(
		        @Param("studentId") String studentId,
		        @Param("subjectCode") String subjectCode,
		        @Param("examSubType") String examSubType);
	List<StudentExam> findByStudent_StudentIdInAndExam_ExamTypeAndExam_Semester_SemesterCode(List<String> studentIds,
			String string, String semesterCode);
	List<StudentExam> findByStudent_StudentIdAndExam_Semester_SemesterCode(String studentId, String semesterCode);
	List<StudentExam> findByStudent_StudentIdInAndExam_ExamType(List<String> studentIds, String string);
	List<StudentExam> findByStudent_StudentIdAndExam_ExamType(String studentId, String string);
	
	
	


}