package com.sts.service.impl.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.dto.StudentRequest;
import com.sts.exceptions.StudentRequestValidationException;
import com.sts.exceptions.StudentValidationException;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentRequestValidator implements Validator<StudentRequest>{

	private final StudentRepository studentRepository;
	private final DepartmentIdValidator departmentIdValidator;
	public StudentRequestValidator(StudentRepository studentRepository, DepartmentIdValidator departmentIdValidator) {
		this.studentRepository = studentRepository;
		this.departmentIdValidator = departmentIdValidator;
	}

	@Override
	public boolean validateAndGetResult(StudentRequest object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void validate(StudentRequest studentRequest) {
	    log.info("Starting validation for StudentRequest: {}", studentRequest);

	    if (studentRequest == null) {
	        log.warn("Validation failed: StudentRequest object is null");
	        throw new StudentRequestValidationException(
	                ErrorCodeEnum.STUDENT_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.STUDENT_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
	    }

	    log.info("Validating student ID: {}", studentRequest.getStudentId());

	    if (studentRequest.getStudentId() == null || studentRequest.getStudentId().isEmpty()) {
	        log.warn("Validation failed: Student ID cannot be null or empty");
	        throw new StudentRequestValidationException(
	                ErrorCodeEnum.STUDENT_ID_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.STUDENT_ID_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
	    }

	    log.debug("Validating department ID: {}", studentRequest.getDepartmentId());

	    departmentIdValidator.validate(studentRequest.getDepartmentId());
	    

	    log.info("Validation successful for Student ID '{}' and Department ID '{}'", 
	             studentRequest.getStudentId(), studentRequest.getDepartmentId());

	}



	
}
