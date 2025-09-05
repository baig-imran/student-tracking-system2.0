package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uId;  // unique id for an attendance for a student	
	
    private LocalDate attendanceDate; 
    private Integer period; 
    private Boolean isPresent;
    
    @ManyToOne
    @JoinColumn(name = "subject_code", nullable = false)
    private SemesterSubject semesterSubject; 
    
    @ManyToOne
    @JoinColumn(name="department_id", nullable = false)
    private Department department;
    
    @ManyToOne
	@JoinColumn(name="student_id", nullable = false)
	private Student student;
    
    @ManyToOne
	@JoinColumn(name="semester_code", nullable = false)
	private Semester semester;
   
}