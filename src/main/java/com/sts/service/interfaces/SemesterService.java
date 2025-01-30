package com.sts.service.interfaces;

import com.sts.dto.SemesterRequest;
import com.sts.dto.SemesterResponse;
import com.sts.entity.Semester;

public interface SemesterService{
	
	Semester getSemester(String semesterCode);
	SemesterResponse saveSemester(SemesterRequest semesterRequest);
	
	
	
	


}
