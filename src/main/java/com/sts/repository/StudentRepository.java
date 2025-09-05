package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sts.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
	
	Student getStudentByStudentId(String studentId);
	List<Student> findAllByStudentIdIn(List<String> studentIds);
	boolean existsByStudentId(String studentId);

	List<Student> findAllByFaculty_FacultyId(String facultyId);
	

	
}