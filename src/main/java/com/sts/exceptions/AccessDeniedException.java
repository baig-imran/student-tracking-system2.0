package com.sts.exceptions;

public class AccessDeniedException extends RuntimeException{
	
	public AccessDeniedException(String message){
		super(message);
	}

}

