package com.sts.dto.semestersubject;

import lombok.Data;

@Data
public class FacultySemesterSubjectStudentsGetRequest {
	
	private String facultyId;
	private String semesterCode;
	private String subjectCode;


}
