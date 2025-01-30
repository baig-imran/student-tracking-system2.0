package com.sts.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private String departmentId;
    
    @OneToMany(mappedBy = "faculty", cascade=CascadeType.ALL)
    List<Student> students; //list of mentoring students
    
    @ManyToMany(mappedBy = "faculties") // Field name in Semester
    private List<Semester> semesters;
    
    
    
        
   
}
