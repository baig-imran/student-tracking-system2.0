package com.sts.entity;

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
public class StudentSubject {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
	
    @ManyToOne
    @JoinColumn(name = "subject_code")
    private SemesterSubject semesterSubject;
	

}
