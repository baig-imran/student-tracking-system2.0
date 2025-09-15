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

	List<String> getStudentsByExamCode(String examCode);

	GetStudentAllSemesterExamDetailsRes getAllSemestersExamDataByStudentId(String studentId);

	GetStudentAllSemesterInternalExamDetailsRes getAllSemestersInternalExamDataByStudentId(String studentId);

	List<String> getInternalExamQualifiedStudentsBySubjectCode(String subjectCode);
	List<String> getInternalExamDisQualifiedStudentsBySubjectCode(String subjectCode);


	String addMultipleSubjectExamDataUpload(List<AddStudentsExamDataReq> reqList);

	GetStudentCompleteResultRes getOverallMarksByStudentId(String studentId);

	List<String> getSEEFailedStudentsByExamCode(String examCode);

	List<GetLowInternalMarksStudentsByFacultyIdRes> getLowInternalMarksStudentsByFacultyId(String facultyId);

	List<GetInternalMarksByStudentIdAndSemesterCodeRes> getInternalMarksByStudentIdAndSemesterCode(String studentId,
			String semesterCode);

	List<GetLowExternalMarksStudentsByFacultyIdRes> getLowExternalMarksStudentsByFacultyId(String facultyId);

	List<GetStudentsWithSupplyByFacultyIdRes> getStudentsWithSupplyByFacultyId(String facultyId);

	List<GetSupplyExamDetailsByStudentIdRes> getSupplyExamDetailsByStudentId(String studentId);

	
	
	
	
	
	
	
	
}
