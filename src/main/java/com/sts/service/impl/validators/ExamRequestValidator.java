package com.sts.service.impl.validators;


import org.springframework.stereotype.Service;

import com.sts.dto.exam.AddExamRequest;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExamRequestValidator implements Validator<AddExamRequest>{

	private final StudentRepository studentRepository;
	
	public ExamRequestValidator(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}
	@Override
	public void validate(AddExamRequest addExamRequest) {
	    log.debug("Starting validation for AddExamRequest: {}", addExamRequest);

		
		

		
	}
	@Override
	public boolean validateAndGetResult(AddExamRequest addExamRequest) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}