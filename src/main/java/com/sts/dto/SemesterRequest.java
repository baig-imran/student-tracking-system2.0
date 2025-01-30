package com.sts.dto;

import java.time.LocalDate;
import java.util.List;

import com.sts.entity.Faculty;
import com.sts.entity.Student;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class SemesterRequest {

	private String semesterCode;
	private Integer semesterNumber; //1,2
	private Integer studyYear;	// 1,2,3,4
	private Integer academicYear;//2020, 2021 ...
	private String regulation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String batch;
	private String departmentId;
	
	private List<String> students;  // List of students for the semester
	private List<String> faculties;  // List of students for the semester

}