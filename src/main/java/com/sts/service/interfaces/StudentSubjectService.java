package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;

public interface StudentSubjectService {
	
	
	void saveStudentSubject();
	List<AddStudentsToSubjectRes> addStudentsToSubject(List<AddStudentsToSubjectReq> req);
	List<String> getSubjectStudentsBySubjectCode(String subjectCode);
	

}
