package com.sts.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

	@Id
	private String studentId;  

	private String studentName;
	private String fatherName;
	private String fatherMobileNumber;
	private String studentMobileNumber;
	private Boolean isGraduated = false;
	private String regulation;
	private String batch;


	@ManyToOne
	@JoinColumn(name="department_id", nullable = false)
	private Department department;
	
	@ManyToOne
    @JoinColumn(name="mentor")
    private Faculty faculty;


	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	List<Attendance> attendance; 
	
	
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	List<Exam> exams;
	
	
	@ManyToMany(mappedBy = "students")  // Reverse side of the many-to-many relationship
    private List<Semester> semesters;  // List of students for the semester
	
	
	
	



}
