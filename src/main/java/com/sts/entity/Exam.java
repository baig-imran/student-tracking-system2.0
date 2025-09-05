package com.sts.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

//@Table(name = "exam", uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "subject_code"})})
//The above line is commented because, constraint was added directly using sql script
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID
	private Long examUid;
    
	private String examCode; // Common code for all student	
	private String examType;  // Internal/External
	private String examSubType;
	private String examName;  // Assignment, mid, semend, supply,
	private LocalDate examDate;
	private Double passMarks;
	
	@ManyToOne
    @JoinColumn(name = "subject_code", nullable = false)
    private SemesterSubject semesterSubject;
		
	@ManyToOne
	@JoinColumn(name = "semester_code", nullable = false)
	private Semester semester;


	@OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
	private List<StudentExam> studentExams;

	

}
