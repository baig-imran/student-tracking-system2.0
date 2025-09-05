package com.sts.service.interfaces;

import java.util.List;

import com.sts.dto.department.DepartmentCreateRequest;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.DepartmentUpdateRequest;
import com.sts.entity.Department;

public interface DepartmentService{

	 	DepartmentResponse getDepartmentById(String departmentId);
	    List<DepartmentResponse> getAllDepartments();
	    DepartmentResponse saveDepartment(DepartmentCreateRequest request);
	    String bulkCreateDepartments(List<DepartmentCreateRequest> requests);
	    DepartmentResponse updateDepartment(DepartmentUpdateRequest request);
	    String deleteDepartmentById(String departmentId);
	
	


}
