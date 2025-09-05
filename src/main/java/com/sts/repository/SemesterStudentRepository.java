package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Student;
import com.sts.entity.SemesterStudent;

@Repository
public interface SemesterStudentRepository extends JpaRepository< SemesterStudent, Long> {
	
    @Query("SELECT ss.student FROM SemesterStudent ss WHERE ss.semester.semesterCode = :semesterCode")
    List<Student> findStudentsBySemesterCode(@Param("semesterCode") String semesterCode);


}
