package com.sts.dto.exam;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AddExamRequest {

	  	private String examCode; // Common code for all students, can be same as subjectCode
	    private String examType;
	    private String examSubType;
	    private String examName;
	    private LocalDate examDate;
	    private String subjectCode;
	    private String passMarks;

    
   

}
