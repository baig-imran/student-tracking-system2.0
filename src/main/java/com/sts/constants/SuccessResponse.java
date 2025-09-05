package com.sts.constants;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class SuccessResponse<T> {
	
	private int status;
	private String message;
	private T data;
    
    public SuccessResponse() {
    	
    }
	public SuccessResponse(T data, String message, int status) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
    

}
