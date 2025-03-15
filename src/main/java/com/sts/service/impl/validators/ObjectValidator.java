package com.sts.service.impl.validators;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import com.sts.exceptions.FilterCriteriaException;

public class ObjectValidator {

	 public static boolean isObjectEmpty(Object obj) {
	        if (obj == null) {
	            return true;
	        }
	        for (Field field : obj.getClass().getDeclaredFields()) {
	            field.setAccessible(true);
	            try {
	                if (field.get(obj) != null) {
	                    return false;
	                }
	            } catch (IllegalAccessException e) {
	                throw new RuntimeException("Error accessing field: " + field.getName(), e);
	            }
	        }
	        throw new FilterCriteriaException();
	    }
    }


