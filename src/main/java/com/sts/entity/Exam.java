package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

//@Table(name = "exam", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "subject_code"})})
//The above line is commented because, constraint was added directly using sql script
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID

	private Long examUid;  // Unique to each student
	private String examCode; // Common code for all student
	
	private String examType;
	private String examName;
	private LocalDate examDate;

	private String subjectCode;
	private Double marksObtained;
	
	@ManyToOne
    @JoinColumn( name= "semester_code", nullable = false)
    private Semester semester;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable = false)
	private Student student;
	

}
