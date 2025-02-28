package com.sts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.sts.dto.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {
	
	

    
    
  

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
