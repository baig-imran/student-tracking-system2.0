package com.sts.dto.semester;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class AddSubjectsToSemReq {
    private String semesterCode;
    private String subjectCode; // manually given by user
	private String subjectId;  
	
	private String subjectType;
	private int credits;
	private String facultyId;

 
}




