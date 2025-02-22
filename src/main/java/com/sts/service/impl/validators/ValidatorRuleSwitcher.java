package com.sts.service.impl.validators;

import org.springframework.stereotype.Service;

import com.sts.repository.DepartmentRepository;
import com.sts.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class ValidatorRuleSwitcher{
	
	private DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	
	public ValidatorRuleSwitcher(DepartmentRepository departmentRepository, StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
		this.departmentRepository = departmentRepository;
	}

	public Object getDayName(Object object) {
		
		
		
		return object;        

	}
	
	

}
