package com.sts.service.impl.validators;


import org.springframework.stereotype.Service;

import com.sts.dto.ExamRequest;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExamRequestValidator implements Validator<ExamRequest>{

	private final StudentRepository studentRepository;
	
	public ExamRequestValidator(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}
	@Override
	public void validate(ExamRequest examRequest) {
	    log.debug("Starting validation for ExamRequest: {}", examRequest);

		
		log.info("Validation successful for Student ID '{}' and ExamRequest '{}'", 
				examRequest.getStudentId(), examRequest);

		
	}
	@Override
	public boolean validateAndGetResult(ExamRequest examRequest) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}