package com.sts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sts.constants.ErrorCodeEnum;
import com.sts.dto.CustomErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {
	
	@ExceptionHandler(StudentValidationException.class)
	public ResponseEntity<CustomErrorResponse> handleCustomChekedException(StudentValidationException e){
		log.error("Student validation exception occured: {}",e.toString());
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(e.getErrorCode(),e.getErrorMessage());
		return new ResponseEntity<>(custonExceptionBody, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(DepartmentIdValidationException.class)
	public ResponseEntity<CustomErrorResponse> handleDepartmentIdValidationException(DepartmentIdValidationException e){
		
		log.error("Deparrtment ID validation exception occured: {}",e.toString());
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(e.getErrorCode(),e.getErrorMessage());
		return new ResponseEntity<>(custonExceptionBody, e.getHttpStatus());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomErrorResponse> handleGenericException(Exception e){
		
		log.error("Unknown exception occured: {}",e.getMessage());
 
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(ErrorCodeEnum.UNKNOWN_EXCEPTION.getErrorCode(),ErrorCodeEnum.UNKNOWN_EXCEPTION.getErrorMessage());
		
		return new ResponseEntity<>(custonExceptionBody, HttpStatus.BAD_REQUEST); 
		
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e){
		
		log.error(e.getErrorMessage());
 
		CustomErrorResponse custonExceptionBody = new CustomErrorResponse(ErrorCodeEnum.CUSTOM_EXCEPTION.getErrorCode(),e.getErrorMessage());
		
		return new ResponseEntity<>(custonExceptionBody, e.getHttpStatus()); 
		
	}

}
