package com.sts.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {
	
	Semester getSemesterBySemesterCode(String semesterCode);
	boolean existsBySemesterCode(String semesterCode);
	
	@Query("SELECT s FROM Semester s WHERE :currentDate BETWEEN s.startDate AND s.endDate")
    Optional<Semester> findActiveSemester(@Param("currentDate") LocalDate currentDate);
	
	@Query("SELECT s FROM Semester s WHERE :currentDate BETWEEN s.startDate AND s.endDate")
	List<Semester> findActiveSemesters(@Param("currentDate") LocalDate currentDate);

	
	@Query("""
		    SELECT s FROM Semester s
		    JOIN s.semesterStudents ss
		    JOIN ss.student st
		    WHERE st.id = :studentId
		      AND :currentDate BETWEEN s.startDate AND s.endDate
		""")
		Optional<Semester> findActiveSemesterByStudentIdAndDate(
		    @Param("studentId") String studentId,
		    @Param("currentDate") LocalDate currentDate
		);

}
