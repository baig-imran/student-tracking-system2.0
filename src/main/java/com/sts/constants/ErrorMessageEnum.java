package com.sts.constants;


public enum ErrorMessageEnum {
	 // Generic Errors
    ACCESS_DENIED("Access is denied"),
    BAD_REQUEST("Invalid request parameters"),
    INTERNAL_ERROR("An unexpected error occurred"),
    DATABASE_ERROR("Database operation failed"),
    DUPLICATE_RESOURCE("Resource already exists"),
    EXTERNAL_SERVICE_ERROR("External service error"),
    INVALID_INPUT("Invalid input provided"),
    RESOURCE_NOT_FOUND("Requested resource not found"),
    UNAUTHORIZED_ACCESS("Unauthorized access"),

    // Validation Errors
    STUDENT_NOT_FOUND("Student with ID '%s' not found"),
    STUDENTS_NOT_FOUND("No students found for the given filter criteria"),
    VALIDATION_FAILED("'%s' Validation failed"),
    UNEXPECTED_ERROR("An unexpected error occurred."),
    INVALID_CREDENTIALS("Invalid credentials"),
    EMPTY_REQUEST_OBJECT("Filter criteria/Request object is empty. At least one filter parameter is required"),

    // Unknown or Miscellaneous Errors
    UNKNOWN_EXCEPTION("Unknown Error"),

    // Student Service Errors
    STUDENT_SERVICE("Error in student service implementation"),
    STUDENT_ID_REQUIRED("Student ID is required"),
    STUDENT_REQUIRED("Student cannot be null or empty"),
    STUDENT_ID_NOT_FOUND("Student ID '%s' not found"),
    STUDENT_IDS_NOT_FOUND("Student IDs '%s' not found"),
    DUPLICATE_STUDENT_ID("Student '%s' already exists"),

    // Department Service Errors
    DEPARTMENT_SERVICE("Error in department service implementation"),
    DEPARTMENT_ID_REQUIRED("Department ID is required"),
    DEPARTMENT_ID_NOT_FOUND("Department '%s' not found"),
    DUPLICATE_DEPARTMENT_ID("Department '%s' already exists"),
    DEPARTMENT_REQUIRED("Department cannot be null or empty"),

    // Faculty Service Errors
    FACULTY_SERVICE("Error in faculty service implementation"),
    FACULTY_ID_REQUIRED("Faculty ID is required"),
    FACULTY_ID_NOT_FOUND("Faculty ID '%s' not found"),
    DUPLICATE_FACULTY_ID("Faculty ID '%s' already exists"),
    FACULTY_REQUIRED("Faculty cannot be null or empty"),

    // Semester Service Errors
    SEMESTER_SERVICE("Error in semester service implementation"),
    SEMESTER_CODE_REQUIRED("Semester code is required"),
    SEMESTER_CODE_NOT_FOUND("Semester code '%s' not found"),
    DUPLICATE_SEMESTER_CODE("Semester code '%s' already exists"),
    SEMESTER_REQUIRED("Semester cannot be null or empty"),

    // Subject Service Errors
    SUBJECT_SERVICE("Error in subject service implementation"),
    SUBJECT_ID_REQUIRED("Subject ID is required"),
    SUBJECT_ID_NOT_FOUND("Subject ID '%s' not found"),
    SUBJECT_REQUIRED("Subject cannot be null or empty"),
    DUPLICATE_SUBJECT_CODE("Subject ID '%s' already exists"),
    SUBJECTS_NOT_FOUND("No subjects found for the given specification"),

    // User Service Errors
    USER_SERVICE("Error in user service implementation"),
    USER_ID_REQUIRED("User ID is required"),
    USER_ID_NOT_FOUND("User ID '%s' not found"),
    DUPLICATE_USER("User '%s' already exists"),
    USER_REQUIRED("User cannot be null or empty"),
    USER_NOT_FOUND("User '%s' not found"),

    // Exam Service Errors
    EXAM_NOT_FOUND("Exam record not found: '%s'"),
    EXAMS_NOT_FOUND_FOR_SPECIFICATION("Exams record not found for the given specification '%s' "),
    DUPLICATE_EXAM_CODE("Exam ID '%s' already exists"),
    FAILED_TO_SAVE_EXAM("Error occurred while saving exam"),
    FAILED_TO_UPDATE_EXAM("Error occurred while updating exam"),
    FAILED_TO_FETCH_EXAMS("Error occurred while fetching exam data"),

    // Semester-Subject Relationship Errors
    SEMESTER_SUBJECT_NOT_FOUND("Semester Subject not found: '%s'"),

    // Student-Exam Mismatch Errors
    STUDENT_EXAM_MISSMATCH("Student '%s' and Exam '%s' doesn't match"),
    STUDENT_SEMESTER_MISSMATCH("Student '%s' and Semester '%s' doesn't match"),
    DUPLICATE_STUDENT_EXAMDATA("The exam data for the given students already exists for exam '%s'"),

    // Student-Subject Mismatch Errors
    STUDENT_SUBJECT_MISSMATCH("Student '%s' is not registered for Subject '%s'"),

    // Faculty-Semester-Subject Errors
    FACULTY_SEMESTER_SUBJECTS_NOT_FOUND("Semester subjects not found for faculty");

    private final String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
    	if (message.contains("%s") && (args == null || args.length == 0)) {
            return message.replaceAll(" '%s' ", "");  // This logic removes '%s' from the message, because if no argument is passed, then '%s' will be displayed as it is. 
        }
        return (args != null && args.length > 0) ? String.format(message, args) : message;
    }
}
