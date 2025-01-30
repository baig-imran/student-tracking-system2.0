package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
	
	Student getStudentByStudentId(String studentId);
	List<Student> findAllByStudentIdIn(List<String> studentIds);
	
}