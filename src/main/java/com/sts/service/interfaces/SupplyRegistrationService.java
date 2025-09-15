package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.exam.supplyregistration.RegisterSupplyStudentsReq;

public interface SupplyRegistrationService {

	String registerStudentsForSupplyExam(RegisterSupplyStudentsReq req);


	List<String> getSupplyRegisteredStudentIdsByExamCode(String examCode);


	String registerStudentsForSupplyExamInBulk(List<RegisterSupplyStudentsReq> reqList);
	

}
