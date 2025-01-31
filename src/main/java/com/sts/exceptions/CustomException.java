package com.sts.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomException extends RuntimeException{
	
	
	private String errorMessage;
	private HttpStatus httpStatus;
	public CustomException(String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

}