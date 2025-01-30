package com.sts.service.interfaces;

import com.sts.dto.ExamRequest;
import com.sts.dto.ExamResponse;

public interface ExamService {
	
	
	 ExamResponse saveExam(ExamRequest examRequest);

}
