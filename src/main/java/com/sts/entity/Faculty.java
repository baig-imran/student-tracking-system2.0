//package com.sts.entity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
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
//public class Faculty {
//
//	@Id
//    private String facultyId;
//    private String facultyName;
//    private String facultyMobileNumber;
//
//    
//    @ManyToOne
//    @JoinColumn(name="department_id", nullable = false)
//    private Department department;
//    
//    @OneToMany(mappedBy = "faculty", cascade=CascadeType.ALL)
//    List<Student> students; //list of mentoring students
//    
//    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
//    private List<SemesterFaculty> semesterFaculties = new ArrayList<>();
// 
//}


package com.sts.entity;

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
public class Faculty {

	@Id
    private String facultyId;
    private String facultyName;
    private String facultyMobileNumber;

    
    @ManyToOne
    @JoinColumn(name="department_id", nullable = false)
    private Department department;
    
    @OneToMany(mappedBy = "faculty", cascade=CascadeType.ALL)
    List<Student> students= new ArrayList<>(); //list of mentoring students
    
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<SemesterFaculty> semesterFaculties = new ArrayList<>();
    
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<SemesterSubject> semesterSubjects = new ArrayList<>();
 
}

