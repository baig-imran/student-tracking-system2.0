package com.sts.dto.faculty;

import java.util.ArrayList;
import java.util.List;

import com.sts.entity.Department;
import com.sts.entity.SemesterFaculty;
import com.sts.entity.Student;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
public class CreateFaculty {
	
	private String facultyId;
    private String facultyName;
    private String facultyMobileNumber;
    private String departmentId;
    

}
