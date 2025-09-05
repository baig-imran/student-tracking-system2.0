//package com.sts.entity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SemesterSubject {
//	
//	@Id
//    private String subjectCode;
//
//    @ManyToOne
//    @JoinColumn(name = "semester_code")
//    private Semester semester;
//
//    @ManyToOne
//    @JoinColumn(name = "subject_code")
//    private Subjects subject;
//
//
//}



package com.sts.entity;

import java.util.List;

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
public class SemesterSubject { //Subjects of a semester
	
	@Id
    private String subjectCode;
	
	private String subjectType; //Subject/Lab/Elective Subject/Elective Lab
	
	private int credits;
	
//	private String subjectSubType; //TODO: Add these field in the implementations
//	
//	private String passMarks;		//TODO: Add these field in the implementations

    @ManyToOne
    @JoinColumn(name = "semester_code")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subjects subject;

    @ManyToOne
    @JoinColumn(name = "faculty_id") 
    private Faculty faculty;
    
    @OneToMany(mappedBy = "semesterSubject")
    private List<StudentSubject> students;
    
    
}