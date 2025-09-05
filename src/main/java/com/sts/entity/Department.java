package com.sts.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Department {

	@Id
	private String departmentId;
    private String departmentName;
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	List<Student> students  = new ArrayList<>();
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	List<Faculty> faculties  = new ArrayList<>();
    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	List<Semester> semesters  = new ArrayList<>();

    
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    List<Subjects> subjects  = new ArrayList<>();

}


