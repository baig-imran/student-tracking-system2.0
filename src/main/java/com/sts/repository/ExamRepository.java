package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Exam;
import com.sts.entity.Student;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

	Exam getExamByExamCode(String examCode);
	Exam getExamByStudent(Student student);
    List<Exam> findAllByExamCodeIn(List<String> examIds);
	
	
	
}