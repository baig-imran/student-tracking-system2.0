package com.sts.constants;

public enum ErrorCodeEnum {
	UNKNOWN_EXCEPTION("00000","Unknown Error"),
	
	STUDENT_ID_REQUIRED("10001","Student ID cannot be null or empty"),
	STUDENT_ID_NOT_FOUND("10002","Student ID does not exists"),
	STUDENT_REQUIRED("10003","Student cannot be null or empty"),
	
	DEPARTMENT_ID_REQUIRED("20001","Department ID cannot be null or empty"),
	DEPARTMENT_ID_NOT_FOUND("20002","Department ID does not exists"),
	DEPARTMENT_REQUIRED("20003","Department cannot be null or empty"),
	
	FACULTY_ID_REQUIRED("30001", "Faculty ID is required and cannot be null or empty"),
	FACULTY_ID_NOT_FOUND("30002", "Faculty ID does not exist"),
	FACULTY_REQUIRED("30003", "Faculty cannot be null or empty"),

	SEMESTER_CODE_REQUIRED("40001", "Semester code is required and cannot be null or empty"),
	SEMESTER_CODE_NOT_FOUND("40002", "Semester code does not exist"),
	SEMESTER_REQUIRED("40003", "Semester cannot be null or empty"),

	SUBJECT_CODE_REQUIRED("50001", "Subject code is required and cannot be null or empty"),
	SUBJECT_CODE_NOT_FOUND("50002", "Subject code does not exist"),
	SUBJECT_REQUIRED("50003", "Subject cannot be null or empty")
	
	;

	private String errorCode;
	private String errorMessage;
	ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode=errorCode;
		this.errorMessage = errorMessage;
		
	}
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	

}
