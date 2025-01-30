package com.sts.service.impl.validators;

import javax.security.auth.Subject;

import org.springframework.stereotype.Service;

import com.sts.entity.Student;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubjectValidator implements Validator<Subject>{

	
	@Override
	public void validate(Subject subject) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean validateAndGetResult(Subject object) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
