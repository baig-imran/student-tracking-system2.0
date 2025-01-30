package com.sts.service.impl.validators;


import org.springframework.stereotype.Service;

import com.sts.entity.Department;
import com.sts.repository.DepartmentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentValidator implements Validator<Department>{

	private final DepartmentRepository departmentRepository;
	
	public DepartmentValidator(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	public void validate(Department department) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean validateAndGetResult(Department object) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
