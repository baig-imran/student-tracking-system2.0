package com.sts.constants;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ValidatorRuleStatusEnum {
	
	
	 STUDENT_ID_VALIDATOR("STUDENT_ID_VALIDATOR"),
	STUDENT_REQUEST_VALIDATOR("STUDENT_REQUEST_VALIDATOR")
	;
	
	private final String ruleName;
	

	private ValidatorRuleStatusEnum(String ruleName) {
		this.ruleName = ruleName;
	}
	

}
