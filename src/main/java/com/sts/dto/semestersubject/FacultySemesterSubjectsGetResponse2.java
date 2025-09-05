package com.sts.dto.semestersubject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FacultySemesterSubjectsGetResponse2 {
	
	private String facultyId;
	private List<FacultySemesterSubjects2> facultySemesterSubjects = new ArrayList<>();
	@Data
	public static class FacultySemesterSubjects2{
		private String subjectCode;
		private String subjectTitle;
		private String semesterCode;
		private String departmentId;
		
	}
}