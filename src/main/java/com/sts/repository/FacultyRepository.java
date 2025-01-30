package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {

	Faculty getFacultyByFacultyId(String facultyId);
    List<Faculty> findAllByFacultyIdIn(List<String> facultyIds);
	
	
	
}