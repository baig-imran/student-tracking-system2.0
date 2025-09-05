package com.sts.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Semester {

	@Id
	private String semesterCode;
	private Integer semesterNumber; //1,2
	private Integer studyYear;	// 1,2,3,4
	private Integer semesterSerialNumber; //1,2,...8
	private Integer academicYear;//2020, 2021 ...
	private String regulation;
	private LocalDate startDate;
	private LocalDate endDate;
	private String batch;
	
	@ManyToOne
	@JoinColumn(name="department_id", nullable = false)
	private Department department;
	
	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	private List<Exam> exams = new ArrayList<>();

	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	private List<Attendance> attendance = new ArrayList<>();

	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	private List<SemesterStudent> semesterStudents = new ArrayList<>();

	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	private List<SemesterFaculty> semesterFaculties = new ArrayList<>();

	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	private List<SemesterSubject> semesterSubjects = new ArrayList<>();


}
