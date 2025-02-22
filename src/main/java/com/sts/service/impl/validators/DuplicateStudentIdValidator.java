package com.sts.service.impl.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.exceptions.CustomException;
import com.sts.repository.DepartmentRepository;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class DuplicateStudentIdValidator implements Validator<String>{
	
	private DepartmentRepository departmentRepository;
	private final StudentRepository studentRepository;
	
	public DuplicateStudentIdValidator(DepartmentRepository departmentRepository, StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public void validate(String studentId) {

	    log.info("Validating student ID: {}", studentId);
	    
	    if(studentRepository.existsByStudentId(studentId)) {
			throw new CustomException(ErrorCodeEnum.DUPLICATE_STUDENT_ID.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}
	    

	}

	@Override
	public boolean validateAndGetResult(String studentId) {
		
		return false;
	}
	
	
	

}
