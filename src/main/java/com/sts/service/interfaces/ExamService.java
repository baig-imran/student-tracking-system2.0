package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;

public interface ExamService {
	
	
	 ExamResponse saveExam(ExamRequest examRequest);
	 
	 String updateExam(ExamUpdateRequest examUpdateRequest);

	String saveMultipleExams(List<ExamRequest> examRequests);

	String updateMultipleExams(List<ExamUpdateRequest> examUpdateRequests);

	List<ExamResponse> getExams(ExamRequest filterRequest);
	
	
	

}
