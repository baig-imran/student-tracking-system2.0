package com.sts.constants;


public enum ErrorMessageEnum {
    ACCESS_DENIED("Access is denied"),
    BAD_REQUEST("Invalid request parameters"),
    INTERNAL_ERROR("An unexpected error occurred"),
    DATABASE_ERROR("Database operation failed"),
    DUPLICATE_RESOURCE("Resource already exists"),
    EXTERNAL_SERVICE_ERROR("External service error"),
    INVALID_INPUT("Invalid input provided"),
    RESOURCE_NOT_FOUND("Requested resource not found"),
    UNAUTHORIZED_ACCESS("Unauthorized access"),
    
    STUDENT_NOT_FOUND("Student with ID '%s' not found"),
    VALIDATION_FAILED(" '%s' Validation failed"),
    UNEXPECTED_ERROR("An unexpected error occurred."),
    INVALID_CREDENTIALS("Invalid credentials"),
    EMPTY_SEARCH_PARAMS("Missing search parameters. At least one search parameter is required."),
    
    
    
    UNKNOWN_EXCEPTION("Unknown Error"),
  

    STUDENT_SERVICE("Error in student service implementation"),
    STUDENT_ID_REQUIRED("Student ID is required"),
    STUDENT_REQUIRED("Student cannot be null or empty"),
    STUDENT_ID_NOT_FOUND("Student ID '%s' not found"),
    DUPLICATE_STUDENT_ID("Student '%s' already exists"),

    DEPARTMENT_SERVICE("Error in department service implementation"),
    DEPARTMENT_ID_REQUIRED("Department ID is required"),
    DEPARTMENT_ID_NOT_FOUND("Department '%s' exist"),
    DUPLICATE_DEPARTMENT_ID("Department '%s' already exists"),
    DEPARTMENT_REQUIRED("Department cannot be null or empty"),

    FACULTY_SERVICE("Error in faculty service implementation"),
    FACULTY_ID_REQUIRED("Faculty ID is required "),
    FACULTY_ID_NOT_FOUND("Faculty ID '%s' not found"),
    DUPLICATE_FACULTY_ID("Faculty ID '%s' already exists"),
    FACULTY_REQUIRED("Faculty cannot be null or empty"),

    SEMESTER_SERVICE("Error in semester service implementation"),
    SEMESTER_CODE_REQUIRED("Semester code is required "),
    SEMESTER_CODE_NOT_FOUND("Semester code '%s' not found"),
    DUPLICATE_SEMESTER_CODE("Semester code '%s' already exists"),
    SEMESTER_REQUIRED("Semester cannot be null or empty"),

    SUBJECT_SERVICE("Error in subject service implementation"),
    SUBJECT_CODE_REQUIRED("Subject code is required "),
    SUBJECT_CODE_NOT_FOUND("Subject code '%s' not found"),
    SUBJECT_REQUIRED("Subject cannot be null or empty"),
    DUPLICATE_SUBJECT_CODE("Subject code '%s' already exists"),
    
    USER_SERVICE("Error in user service implementation"),
    USER_ID_REQUIRED("User ID is required "),
    USER_ID_NOT_FOUND("User ID '%s' not found"),
    DUPLICATE_USER("User '%s' already exists"),
    USER_REQUIRED("User cannot be null or empty"), 
    USER_NOT_FOUND("User '%s' not found"), 
    
    EXAM_NOT_FOUND("Exam record not found"),
    FAILED_TO_SAVE_EXAM("Error occurred while saving exam"),
    FAILED_TO_UPDATE_EXAM("Error occurred while updating exam"),
    FAILED_TO_FETCH_EXAMS("Error occurred while fetching exam data")

    ;

    private final String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
    	if (message.contains("%s") && (args == null || args.length == 0)) {
            return message.replaceAll(" '%s'", "");  // This logic removes '%s' from the message, because if no argument is not passed, then '%s' will be displayed as it is. 
        }
        return (args != null && args.length > 0) ? String.format(message, args) : message;
    }
}
