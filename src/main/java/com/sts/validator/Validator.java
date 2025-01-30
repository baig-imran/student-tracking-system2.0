package com.sts.validator;

public interface Validator<T> {
	
	 void validate(T object);
	 
	 boolean validateAndGetResult(T object);
	 

}
