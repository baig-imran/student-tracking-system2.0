package com.sts.constants;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ValidatorRulesEnum {
	
	
	 STUDENT_ID_VALIDATOR("STUDENT_ID_VALIDATOR"),
	STUDENT_REQUEST_VALIDATOR("STUDENT_REQUEST_VALIDATOR"),
	EXAM_REQUEST_VALIDATOR("EXAM_REQUEST_VALIDATOR"),
	ATTENDANCE_REQUEST_VALIDATOR("ATTENDANCE_REQUEST_VALIDATOR")
	
	;
	
	private final String ruleName;
	

	private ValidatorRulesEnum(String ruleName) {
		this.ruleName = ruleName;
	}
	

}
