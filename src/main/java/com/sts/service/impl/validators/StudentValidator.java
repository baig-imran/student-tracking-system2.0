package com.sts.service.impl.validators;

import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.entity.Student;
import com.sts.exceptions.StudentValidationException;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentValidator implements Validator<Student>{

	private final StudentRepository studentRepository;
	private final DepartmentIdValidator departmentIdValidator;
	public StudentValidator(StudentRepository studentRepository, DepartmentIdValidator departmentIdValidator) {
		this.studentRepository = studentRepository;
		this.departmentIdValidator = departmentIdValidator;
	}

	@Override
	public void validate(Student student) {
	    log.debug("Starting validation for Student: {}", student);

	    if (student == null) {
	        log.warn("Validation failed: Student object is null");
	        throw new StudentValidationException(
	                ErrorCodeEnum.STUDENT_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.STUDENT_REQUIRED.getErrorMessage()
	        );
	    }

	    log.debug("Validating student ID: {}", student.getStudentId());

	    if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
	        log.warn("Validation failed: Student ID cannot be null or empty");
	        throw new StudentValidationException(
	                ErrorCodeEnum.STUDENT_ID_REQUIRED.getErrorCode(),
	                ErrorCodeEnum.STUDENT_ID_REQUIRED.getErrorMessage()
	        );
	    }

	    log.debug("Validating department ID for student '{}': {}", student.getStudentId(), student.getDepartment().getDepartmentId());

	    try {
	        departmentIdValidator.validate(student.getDepartment().getDepartmentId());
	        log.info("Validation successful: Department ID '{}' is valid", student.getDepartment().getDepartmentId());
	    } catch (Exception e) {
	        log.warn("Validation failed for department ID '{}': {}", student.getDepartment().getDepartmentId(), e.getMessage(), e);
	        throw e;
	    }

	    log.info("Validation completed successfully for Student ID '{}' with Department ID '{}'", 
	             student.getStudentId(), student.getDepartment().getDepartmentId());

	}

	@Override
	public boolean validateAndGetResult(Student object) {
		// TODO Auto-generated method stub
		return false;
	}


	
}
