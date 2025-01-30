package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sts.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
	
	Department getDepartmentByDepartmentId(String departmentId);
	boolean existsByDepartmentId(String departmentID);
}
