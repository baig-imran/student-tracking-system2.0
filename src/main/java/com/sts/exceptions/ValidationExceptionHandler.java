package com.sts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sts.constants.ErrorCodeEnum;
import com.sts.dto.CustomErrorResponse;
import com.sts.dto.StudentError;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {
	
	@ExceptionHandler(StudentValidationException.class)
	public ResponseEntity<CustomErrorResponse> handleCustomChekedException(StudentValidationException ex){
		log.error("Student validation exception occured: {}",ex.toString());
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(ex.getErrorCode(),ex.getErrorMessage());
		return new ResponseEntity<>(custonExceptionBody, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(DepartmentIdValidationException.class)
	public ResponseEntity<CustomErrorResponse> handleDepartmentIdValidationException(DepartmentIdValidationException ex){
		
		log.error("Deparrtment ID validation exception occured: {}",ex.toString());
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(ex.getErrorCode(),ex.getErrorMessage());
		return new ResponseEntity<>(custonExceptionBody, ex.getHttpStatus());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleGenericException(Exception e){
		
		log.error("Unknown exception occured: {}",e.toString());
 
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(ErrorCodeEnum.UNKNOWN_EXCEPTION.getErrorCode(),e.getMessage());
		
		return new ResponseEntity<>(custonExceptionBody, HttpStatus.BAD_REQUEST); 
		
	}

}
