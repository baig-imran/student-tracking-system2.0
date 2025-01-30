package com.sts.validator;

public abstract  class Validator2<T> {
	
	abstract void validate(T object);
	 
	abstract boolean validateAndGetResult(T object);
	 

}
