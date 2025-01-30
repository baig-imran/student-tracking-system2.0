package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
public class Exams {

    @Id
	private Long examUid;  // Unique to each student
	private Long examCode; // Common code for all student
	
	
	private String examType;
	private String examName;
	private LocalDate examDate;
	private Double marksObtained;
	
	@ManyToOne
    @JoinColumn( name= "semester_code", nullable = false)
    private Semester semester;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable = false)
	private Student student;
	
	
	

}
