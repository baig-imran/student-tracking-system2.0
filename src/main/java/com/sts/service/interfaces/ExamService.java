package com.sts.service.interfaces;

import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;
import com.sts.dto.ExamUpdateRequest;

public interface ExamService {
	
	
	 ExamResponse saveExam(ExamRequest examRequest);
	 
	 boolean updateExam(ExamUpdateRequest examUpdateRequest);

}
