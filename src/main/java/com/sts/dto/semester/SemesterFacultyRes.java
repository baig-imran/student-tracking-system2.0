package com.sts.dto.semester;

import java.util.List;

import lombok.Data;

@Data
public class SemesterFacultyRes {
	
	 private String semesterCode;
	 private List<String> facultyIds;

}
