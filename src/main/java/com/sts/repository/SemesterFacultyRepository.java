package com.sts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Faculty;
import com.sts.entity.SemesterFaculty;

@Repository
public interface SemesterFacultyRepository extends JpaRepository<SemesterFaculty, Long> {
	
    @Query("SELECT sf.faculty FROM SemesterFaculty sf WHERE sf.semester.semesterCode = :semesterCode")
    List<Faculty> findFacultiesBySemesterCode(@Param("semesterCode") String semesterCode);


}
