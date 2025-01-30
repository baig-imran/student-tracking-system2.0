package com.sts.service.impl.validators;

import org.springframework.stereotype.Service;

import com.sts.dto.AttendanceRequest;
import com.sts.validator.Validator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceRequestValidator implements Validator<AttendanceRequest>{

	@Override
	public void validate(AttendanceRequest attendanceRequest) {
		log.debug("Starting validation for AttendanceRequest: {}", attendanceRequest);


		log.info("Validation successful for Student ID '{}' and AttendanceRequest '{}'", 
				attendanceRequest.getStudentId(), attendanceRequest);

	}

	@Override
	public boolean validateAndGetResult(AttendanceRequest object) {
		// TODO Auto-generated method stub
		return false;
	}

}
