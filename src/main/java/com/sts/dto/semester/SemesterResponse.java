package com.sts.dto.semester;

import java.time.LocalDate;

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