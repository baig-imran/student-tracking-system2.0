package com.sts.service.impl.validators;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sts.constants.ErrorCodeEnum;
import com.sts.constants.ValidatorRuleEnum;
import com.sts.dto.StudentCreateRequest;
import com.sts.exceptions.CustomException;
import com.sts.repository.StudentRepository;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentRequestValidator implements Validator<StudentCreateRequest>{

	private final StudentRepository studentRepository;
	private final ApplicationContext applicationContext;
	private final ValidatorRuleStatus validatorRuleStatus;


	

	public StudentRequestValidator(StudentRepository studentRepository, ApplicationContext applicationContext, ValidatorRuleStatus validatorRuleStatus) {
		super();
		this.studentRepository = studentRepository;
		this.applicationContext = applicationContext;
		this.validatorRuleStatus = validatorRuleStatus;
	}

	@Override
	public boolean validateAndGetResult(StudentCreateRequest object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void validate(StudentCreateRequest studentRequest) {

	    if (studentRequest.getStudentId() == null || studentRequest.getStudentId().isEmpty()) {
	        throw new CustomException(
	                ErrorCodeEnum.STUDENT_ID_REQUIRED.getErrorMessage(),
	                HttpStatus.BAD_REQUEST
	        );
	    }
	    
	    if(validatorRuleStatus.isRuleActive(ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getRuleName())){
			 log.info("Validation rule '{}' is active, starting validation for student request.", ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getRuleName());
			Validator<String> validator =  (Validator<String>) applicationContext.getBean(ValidatorRuleEnum.DUPLICATE_STUDENT_ID_VALIDATOR.getValidatorClass());
			validator.validate(studentRequest.getStudentId());
		 }
	    
	    

//	    log.debug("Validating department ID: {}", studentRequest.getDepartmentId());
//
//	    departmentIdValidator.validate(studentRequest.getDepartmentId());
	    

	    log.info("Validation successful for Student ID '{}' and Department ID '{}'", 
	             studentRequest.getStudentId(), studentRequest.getDepartmentId());

	}



	
}
