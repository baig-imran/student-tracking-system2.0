package com.sts.exceptions;

import com.sts.constants.ErrorMessages;

public class FilterCriteriaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FilterCriteriaException() {
		super(ErrorMessages.EMPTY_REQUEST_OBJECT.getMessage());
	}

}