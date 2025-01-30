package com.sts.service.impl.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.exceptions.DepartmentIdValidationException;
import com.sts.exceptions.StudentValidationException;
import com.sts.repository.DepartmentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class DepartmentIdValidator implements Validator<String>{
	
	private DepartmentRepository departmentRepository;
	
	public DepartmentIdValidator(DepartmentRepository departmentRepository) {
		super();
		this.departmentRepository = departmentRepository;
	}

	@Override
	public void validate(String departmentId) {

	    log.info("Validating department ID: {}", departmentId);
	    
	    if (departmentId == null || departmentId.isEmpty()) {
	        log.error("Validation failed, departmentId ID cannot be null or empty");
	        throw new StudentValidationException(
	        		ErrorCodeEnum.DEPARTMENT_ID_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.DEPARTMENT_ID_REQUIRED.getErrorMessage()
	        );
	    }

	    if (!departmentRepository.existsByDepartmentId(departmentId)) {
	        log.error("Validation failed: Department ID '{}' not found", departmentId);
	        throw new DepartmentIdValidationException(
	                ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorCode(),
	                ErrorCodeEnum.DEPARTMENT_ID_NOT_FOUND.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
	    }

	    log.info("Validation successful: Department ID '{}' exists", departmentId);
	    
	}

	@Override
	public boolean validateAndGetResult(String object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	

}
