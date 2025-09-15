package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.department.CreateDepartmentReq;
import com.sts.dto.department.CreateDepartmentRes;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.GetDepartmentByIdRes;
import com.sts.dto.department.UpdateDepartmentReq;
import com.sts.dto.department.UpdateDepartmentRes;

public interface DepartmentService{

	 	GetDepartmentByIdRes getDepartmentById(String departmentId);
	    List<DepartmentResponse> getAllDepartments();
	    CreateDepartmentRes createDepartment(CreateDepartmentReq request);
	    String createDepartmentsInBulk(List<CreateDepartmentReq> requests);
	    UpdateDepartmentRes updateDepartment(UpdateDepartmentReq request);
	    String deleteDepartmentById(String departmentId);
	
	


}
