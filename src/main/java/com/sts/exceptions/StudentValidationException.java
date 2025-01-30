package com.sts.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StudentValidationException extends RuntimeException{
	private String errorCode;
	private String errorMessage;
	
	public StudentValidationException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		
	}
	

}
