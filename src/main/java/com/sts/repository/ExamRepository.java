package com.sts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sts.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {

//	Exam getExamByExamCode(String examCode);
//	Exam getExamByStudent(Student student);
//    List<Exam> findAllByExamCodeIn(List<String> examIds);
//    
//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE student_tracking_system.exam SET marks_obtained = :marksObtained "
//            + "WHERE student_id = :studentId "
//            + "AND subject_code = :subjectCode "
//            + "AND exam_name = :examName", nativeQuery = true)
//    int updateExam(
//            @Param("studentId") String studentId,
//            @Param("subjectCode") String subjectCode,
//            @Param("examName") String examName,
//            @Param("marksObtained") Double marksObtained
//    );
	
	
	Optional<Exam> findByExamCode(String examCode);
	List<Exam> findAllBySemesterSubject_SubjectCode(String subjectCode);

	boolean existsByExamCode(String examCode);


	List<Exam> findAllBySemesterSubject_SubjectCodeAndExamTypeAndExamSubType(String subjectCode, String examType, String examSubType);
	
	
	
}