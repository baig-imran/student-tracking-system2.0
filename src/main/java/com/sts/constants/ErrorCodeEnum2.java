package com.sts.constants;

public enum ErrorCodeEnum2 {
	UNKNOWN_EXCEPTION("00000","Unknown Error"),
	CUSTOM_EXCEPTION("11111",""),
	
	STUDENT_SERVICE("10000","Error in student service implpementation"),
	STUDENT_ID_REQUIRED("10001","Student ID cannot be null or empty"),
	STUDENT_ID_NOT_FOUND("10002","Student ID does not exists"),
	STUDENT_REQUIRED("10003","Student cannot be null or empty"),
	DUPLICATE_STUDENT_ID("10004","Student  with the given ID already exists"),
	
	
    
	
	DEPARTMENT_SERVICE("20000","Error in department service implpementation"),
	DEPARTMENT_ID_REQUIRED("20001","Department ID cannot be null or empty"),
	DEPARTMENT_ID_NOT_FOUND("20002","Department ID does not exists"),
	DEPARTMENT_REQUIRED("20003","Department cannot be null or empty"),
	DUPLICATE_DEPARTMENT_ID("20004", "Department with the given ID already exists"),
	
	FACULTY_SERVICE("30000","Error in faculty service implpementation"),
	FACULTY_ID_REQUIRED("30001", "Faculty ID is required and cannot be null or empty"),
	FACULTY_ID_NOT_FOUND("30002", "Faculty ID does not exist"),
	FACULTY_REQUIRED("30003", "Faculty cannot be null or empty"),
	DUPLICATE_FACULTY_ID("30004", "Faculty with the given ID already exists"),


	SEMESTER_SERVICE("40000","Error in semester service implpementation"),
	SEMESTER_CODE_REQUIRED("40001", "Semester code is required and cannot be null or empty"),
	SEMESTER_CODE_NOT_FOUND("40002", "Semester code does not exist"),
	SEMESTER_REQUIRED("40003", "Semester cannot be null or empty"),
    DUPLICATE_SEMESTER_CODE("40004", "Semester with the given code already exists"),

	
	SUBJECT_SERVICE("50000","Error in subject service implpementation"),
	SUBJECT_CODE_REQUIRED("50001", "Subject code is required and cannot be null or empty"),
	SUBJECT_CODE_NOT_FOUND("50002", "Subject code does not exist"),
	SUBJECT_REQUIRED("50003", "Subject cannot be null or empty"),
    DUPLICATE_SUBJECT_CODE("50004", "Subject with the given code already exists"),

	
	;

	private String errorCode;
	private String errorMessage;
	ErrorCodeEnum2(String errorCode, String errorMessage) {
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
