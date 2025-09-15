package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.exam.AddExamRequest;
import com.sts.dto.exam.AddExamResponse;
import com.sts.dto.exam.ExamsBySpecificationReq;
import com.sts.dto.exam.GetExamsBySpecificationRes;
import com.sts.dto.exam.GetExamsBySubjectCodeRes;
import com.sts.dto.exam.GetExternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetInternalExamsBySubjectCodeRes;
import com.sts.dto.exam.GetSupplyExamsBySubjectCodeRes;
import com.sts.entity.Exam;

public interface ExamService {
	
	
	 AddExamResponse createExam(AddExamRequest addExamRequest);
//	 
//	 String updateExam(ExamUpdateRequest examUpdateRequest);
//
//	String saveMultipleExams(List<AddExamRequest> examRequests);
//
//	String updateMultipleExams(List<ExamUpdateRequest> examUpdateRequests);
//
//	List<AddExamResponse> getExams(AddExamRequest filterRequest);
//	

	List<GetExamsBySubjectCodeRes> getExamsBySubjectCode(String subjectCode);

	String createBulkExams(List<AddExamRequest> requests);

	List<GetExamsBySubjectCodeRes> getExamsBySubjectCodeAndExamTypeAndExamSubType(String subjectCode, String examType,
			String examSubType);

	List<Exam> getExamsBySpecification(ExamsBySpecificationReq req);
	
	GetExamsBySpecificationRes mapExamToGetExamBySpecificationRes(Exam exam);

	List<GetInternalExamsBySubjectCodeRes> getInternalExamsBySubjectCode(String subjectCode);

	List<GetExternalExamsBySubjectCodeRes> getExternalExamsBySubjectCode(String subjectCode);
	List<GetSupplyExamsBySubjectCodeRes> getSupplyExamsBySubjectCode(String subjectCode);

	List<GetSupplyExamsBySubjectCodeRes> getSupplyExamsBySubjectCode(String subjectCode, boolean onlyLatest);
	



	


	
	
	

}
