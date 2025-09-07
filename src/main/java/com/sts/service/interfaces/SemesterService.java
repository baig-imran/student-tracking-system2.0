package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.semester.AddFacultiesToSemReq;
import com.sts.dto.semester.AddFacultiesToSemRes;
import com.sts.dto.semester.AddStudentsToSemReq;
import com.sts.dto.semester.AddStudentsToSemRes;
import com.sts.dto.semester.AddStudentsToSubjectReq;
import com.sts.dto.semester.AddStudentsToSubjectRes;
import com.sts.dto.semester.AddSubjectsToSemReq;
import com.sts.dto.semester.AddSubjectsToSemRes;
import com.sts.dto.semester.SemBasicDetailsReq;
import com.sts.dto.semester.SemBasicDetailsRes;
import com.sts.dto.semester.SemesterCreateRequest;
import com.sts.dto.semester.SemesterDetails;
import com.sts.dto.semester.SemesterOverallDetailsRes;

public interface SemesterService{
	
	SemesterDetails getSemester(String semesterCode);
	SemBasicDetailsRes saveSemester(SemBasicDetailsReq semBasicDetailsReq);
	SemesterDetails getSemesterDetails(String semesterCode);
	List<AddFacultiesToSemRes> addFacultiesToSemester(List<AddFacultiesToSemReq> addFacultiesToSemReq);
	List<AddStudentsToSemRes> addStudentsToSemester(List<AddStudentsToSemReq> req);
	List<AddSubjectsToSemRes> addSubjectsToSemester(List<AddSubjectsToSemReq> req);
	List<AddStudentsToSubjectRes> addStudentsToSubject(List<AddStudentsToSubjectReq> req);
	SemesterOverallDetailsRes getOverallSemesterDetails(String semesterCode);
	String createBulkSemesters(List<SemBasicDetailsReq> semesterRequests);
	
	
	


}
