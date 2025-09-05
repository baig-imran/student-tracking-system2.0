package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.exam.supplyregistration.RegisterSupplyStudentsReq;

public interface SupplyRegistrationService {

	String registerSupplyStudents(RegisterSupplyStudentsReq req);


	List<String> getSupplyRegisteredStudentIdsByExamCode(String examCode);


	String registerBulkSupplyStudents(List<RegisterSupplyStudentsReq> reqList);
	

}
