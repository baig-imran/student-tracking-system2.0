package com.sts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sts.entity.Student;
import com.sts.entity.Subjects;

@Repository
public interface SubjectRepository extends JpaRepository<Subjects, String>, JpaSpecificationExecutor<Subjects> {
	
	
}