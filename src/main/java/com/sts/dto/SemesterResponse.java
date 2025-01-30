package com.sts.dto;

import java.time.LocalDate;
import java.util.List;

import com.sts.entity.Attendance;
import com.sts.entity.Exams;
import com.sts.entity.Faculty;
import com.sts.entity.Student;
import com.sts.entity.SubjectsOfSemester;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class SemesterResponse {

	private String semesterCode;
	private Integer semesterNumber; //1,2
	private Integer studyYear;	// 1,2,3,4
	private Integer academicYear;//2020, 2021 ...
	private String regulation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String batch;
	private String departmentId;
	
//	private List<Student> students;  // List of students for the semester
//	private List<Faculty> faculties;  // List of students for the semester
//	
//	private List<Exams> exams;
//
//
//	private List<Attendance> attendance;
//
//	private List<SubjectsOfSemester> subjectsOfSemester;

}