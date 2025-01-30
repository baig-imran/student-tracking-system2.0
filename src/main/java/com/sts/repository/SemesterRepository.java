package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Department;
import com.sts.entity.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {
	
	Department getSemesterBySemesterCode(String semesterCode);
	boolean existsBySemesterCode(String semesterCode);
}
