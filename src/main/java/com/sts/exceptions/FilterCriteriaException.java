package com.sts.exceptions;

import com.sts.constants.ErrorMessageEnum;

public class FilterCriteriaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FilterCriteriaException() {
		super(ErrorMessageEnum.EMPTY_REQUEST_OBJECT.getMessage());
	}

}