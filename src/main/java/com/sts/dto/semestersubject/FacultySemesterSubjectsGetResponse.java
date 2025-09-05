package com.sts.dto.semestersubject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FacultySemesterSubjectsGetResponse {
	
	private String facultyId;
	private String semesterCode;
	private String departmentId;
	private List<FacultySemesterSubjects> facultySemesterSubjects = new ArrayList<>();
	@Data
	public static class FacultySemesterSubjects{
		private String subjectCode;
		private String subjectTitle;
	}
}