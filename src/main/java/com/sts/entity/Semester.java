package com.sts.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
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

	
	private String departmentId;
	
	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	List<Exam> exams;


	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	List<Attendance> attendance;

	@OneToMany(mappedBy = "semester", cascade = CascadeType.ALL)
	List<SubjectsOfSemester> subjectsOfSemester;


	@ManyToMany
	@JoinTable(
			name = "student_semester",  // Join table name
			joinColumns = @JoinColumn(name = "semester_code"),  // Foreign key for Semester
			inverseJoinColumns = @JoinColumn(name = "student_id")  // Foreign key for Student
			)
	private List<Student> students;  // List of students for the semester

	@ManyToMany
	@JoinTable(
			name = "faculty_semester",  // Join table name
			joinColumns = @JoinColumn(name = "semester_code"),  // Foreign key for Semester
			inverseJoinColumns = @JoinColumn(name = "faculty_id")  // Foreign key for Student
			)
	private List<Faculty> faculties;  // List of students for the semester





}
