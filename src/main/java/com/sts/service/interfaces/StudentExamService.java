package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.exam.studentexam.AddMultipleSubjectExamDataUploadReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterInternalExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentCompleteResultRes;

public interface StudentExamService {

	AddStudentsExamDataRes addStudentsExamData(AddStudentsExamDataReq req);

	List<String> getExamStudentsByExamCode(String examCode);

	GetStudentAllSemesterExamDetailsRes getStudentAllSemesterExamDetails(String studentId);

	GetStudentAllSemesterInternalExamDetailsRes getStudentAllSemesterInternalExamDetails(String studentId);

	List<String> getInternalExamQualifiedStudentsBySubject(String subjectCode);


	String addMultipleSubjectExamDataUpload(List<AddStudentsExamDataReq> reqList);

	GetStudentCompleteResultRes getStudentOverallMarks(String studentId);

	List<String> getSEEFailedStudentsByExam(String examCode);
	
	
	
	
	
}
