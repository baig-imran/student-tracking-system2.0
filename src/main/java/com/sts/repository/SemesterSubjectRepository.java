package com.sts.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sts.entity.Faculty;
import com.sts.entity.SemesterSubject;
import com.sts.entity.Student;
import com.sts.entity.StudentSubject;

@Repository
public interface SemesterSubjectRepository extends JpaRepository< SemesterSubject, String> {

	@Query("SELECT ss FROM SemesterSubject ss " +
			"JOIN ss.semester s " +
			"JOIN ss.faculty f " +
			"WHERE f.facultyId = :facultyId " +
			"AND :currentDate BETWEEN s.startDate AND s.endDate")
	List<SemesterSubject> findActiveSemesterStudentsByFacultyIdSemesterCodeSubjectCode(
			@Param("facultyId") String facultyId,
			@Param("currentDate") LocalDate currentDate);

	@Query("SELECT ssj " +
			"FROM StudentSubject ssj " +
			"JOIN ssj.semesterSubject ss " +
			"JOIN ss.semester s " +
			"JOIN ss.faculty f " +
			"WHERE f.facultyId = :facultyId " +
			"AND s.semesterCode = :semesterCode " +
			"AND ss.subjectCode = :subjectCode " +
			"AND :currentDate BETWEEN s.startDate AND s.endDate")
	List<StudentSubject> findActiveSemesterSubjectStudentsByFacultyIdSemesterCodeSubjectCode(
			@Param("facultyId") String facultyId,
			@Param("semesterCode") String semesterCode,
			@Param("subjectCode") String subjectCode,
			@Param("currentDate") LocalDate currentDate
			);

	@Query("SELECT ss FROM SemesterSubject ss " +
			"JOIN ss.semester s " +
			"JOIN ss.faculty f " +
			"WHERE f.facultyId = :facultyId " +
			"AND :currentDate BETWEEN s.startDate AND s.endDate")
	List<SemesterSubject> findActiveSemesterSubjectsByFacultyId(
			@Param("facultyId") String facultyId,
			@Param("currentDate") LocalDate currentDate);


	Optional<List<SemesterSubject>> getAllByFaculty(Faculty faculty);

	List<SemesterSubject> findByFacultyAndSemester_StartDateLessThanEqualAndSemester_EndDateGreaterThanEqual(
			Faculty faculty, LocalDate start, LocalDate end);

	List<SemesterSubject> findByFaculty_facultyIdAndSemester_semesterCode(String facultyId, String semesterCode);








}
