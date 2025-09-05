package com.sts.dto.semestersubject;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FacultySemesterSubjectsGetRequest {
	
	private String facultyId;
	private String semesterCode;
}