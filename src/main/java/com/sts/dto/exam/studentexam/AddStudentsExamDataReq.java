package com.sts.dto.exam.studentexam;

import java.util.List;

import lombok.Data;

@Data
public class AddStudentsExamDataReq {
	
	private String examCode;
	private List<StudentExamMap> studentsExamData;
	
	@Data
	public static class StudentExamMap {
		
		private String studentId;
		private Double marksObtained;
		private Boolean isPresent;
	}
   

}
