package com.sts.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentResponse {
	
	 	private String studentId;
	    private String studentName;
	    private String fatherName;
	    private String fatherMobileNumber;
	    private String studentMobileNumber;
	    private Boolean isGraduated;
	    private String regulation;
	    private String batch;
	    private String departmentId; // Reference by ID
	    private String mentorId; // Reference by ID
//	    private List<String> semesterIds; // List of semester codes

}
