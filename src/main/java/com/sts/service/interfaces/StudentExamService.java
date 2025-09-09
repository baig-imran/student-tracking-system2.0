package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.exam.studentexam.AddMultipleSubjectExamDataUploadReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataReq;
import com.sts.dto.exam.studentexam.AddStudentsExamDataRes;
import com.sts.dto.exam.studentexam.GetInternalMarksByStudentIdAndSemesterCodeRes;
import com.sts.dto.exam.studentexam.GetLowExternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetLowInternalMarksStudentsByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentAllSemesterInternalExamDetailsRes;
import com.sts.dto.exam.studentexam.GetStudentCompleteResultRes;
import com.sts.dto.exam.studentexam.GetStudentsWithSupplyByFacultyIdRes;
import com.sts.dto.exam.studentexam.GetSupplyExamDetailsByStudentIdRes;

public interface StudentExamService {

	AddStudentsExamDataRes addStudentsExamData(AddStudentsExamDataReq req);

	List<String> getExamStudentsByExamCode(String examCode);

	GetStudentAllSemesterExamDetailsRes getStudentAllSemesterExamDetails(String studentId);

	GetStudentAllSemesterInternalExamDetailsRes getStudentAllSemesterInternalExamDetails(String studentId);

	List<String> getInternalExamQualifiedStudentsBySubject(String subjectCode);
	List<String> getInternalExamDisQualifiedStudentsBySubject(String subjectCode);


	String addMultipleSubjectExamDataUpload(List<AddStudentsExamDataReq> reqList);

	GetStudentCompleteResultRes getStudentOverallMarks(String studentId);

	List<String> getSEEFailedStudentsByExam(String examCode);

	List<GetLowInternalMarksStudentsByFacultyIdRes> getLowInternalMarksStudentsByFacultyId(String facultyId);

	List<GetInternalMarksByStudentIdAndSemesterCodeRes> getInternalMarksByStudentIdAndSemesterCode(String studentId,
			String semesterCode);

	List<GetLowExternalMarksStudentsByFacultyIdRes> getLowExternalMarksStudentsByFacultyId(String facultyId);

	List<GetStudentsWithSupplyByFacultyIdRes> getStudentsWithSupplyByFacultyId(String facultyId);

	List<GetSupplyExamDetailsByStudentIdRes> getSupplyExamDetailsByStudentId(String studentId);

	
	
	
	
	
	
	
	
}
