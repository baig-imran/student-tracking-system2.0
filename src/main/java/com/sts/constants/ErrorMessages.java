package com.sts.constants;


public enum ErrorMessages {
	//Generic / System
	ACCESS_DENIED("Access is denied"),
	BAD_REQUEST("Invalid request parameters"),
	DATABASE_ERROR("Database operation failed"),
	DUPLICATE_RESOURCE("Resource already exists"),
	EXTERNAL_SERVICE_ERROR("External service error"),
	INTERNAL_ERROR("An unexpected internal error occurred"),
	INVALID_INPUT("Invalid input provided"),
	RESOURCE_NOT_FOUND("Requested resource not found"),
	UNAUTHORIZED_ACCESS("Unauthorized access"),
	UNKNOWN_EXCEPTION("Unknown error occurred"),
	VALIDATION_FAILED("Validation failed: %s "),
	EMPTY_REQUEST_OBJECT("Request object is empty. At least one field is required"),

	//CRUD Operations
	CREATE_OPERATION_FAILED("Failed to create %s "),
	READ_OPERATION_FAILED("Failed to fetch %s "),
	UPDATE_OPERATION_FAILED("Failed to update %s "),
	DELETE_OPERATION_FAILED("Failed to delete %s "),
	BULK_CREATE_FAILED("Bulk create failed for %s "),
	BULK_UPDATE_FAILED("Bulk update failed for %s "),
	BULK_DELETE_FAILED("Bulk delete failed for %s "),
	BULK_OPERATION_FAILED("Bulk operation failed for %s "),

	//Relationship / Association
	RELATIONSHIP_NOT_FOUND("Relationship between %s and %s not found"),
	RELATIONSHIP_ALREADY_EXISTS("The relationship between %s and %s already exists"),
	ENTITY_MISMATCH(" %s '%s' does not match with %s '%s'"),
	DATA_INCONSISTENCY("Data inconsistency detected for %s "),

	//Authentication / Authorization
	INVALID_CREDENTIALS("Invalid credentials"),
	SESSION_EXPIRED("Session expired. Please login again"),
	OPERATION_NOT_SUPPORTED("Operation not supported for %s "),

	//File / Data Handling
	FILE_UPLOAD_FAILED("Failed to upload file"),
	FILE_DOWNLOAD_FAILED("Failed to download file"),
	FILE_PROCESSING_ERROR("Error occurred while processing file"),
	TEMPLATE_GENERATION_FAILED("Failed to generate template"),

	//External Integrations
	THIRD_PARTY_API_ERROR("Third-party API error occurred"),
	NETWORK_ERROR("Network error occurred while communicating with external service"),
	TIMEOUT_ERROR("Request timed out while waiting for response"),

	//Student Service Errors
	STUDENT_SERVICE("Error in student service implementation"),
	STUDENT_ID_REQUIRED("Student ID is required"),
	STUDENT_REQUIRED("Student cannot be null or empty"),
	STUDENT_ID_NOT_FOUND("Student ID '%s' not found"),
	STUDENT_IDS_NOT_FOUND("Student IDs '%s' not found"),
	DUPLICATE_STUDENT_ID("Student '%s' already exists"),
	STUDENT_NOT_FOUND("Student with ID '%s' not found"),
	STUDENTS_NOT_FOUND("No students found for the given filter criteria"),

	//Department Service Errors
	DEPARTMENT_SERVICE("Error in department service implementation"),
	DEPARTMENT_ID_REQUIRED("Department ID is required"),
	DEPARTMENT_REQUIRED("Department cannot be null or empty"),
	DEPARTMENT_ID_NOT_FOUND("Department '%s' not found"),
	DUPLICATE_DEPARTMENT_ID("Department '%s' already exists"),

	//Faculty Service Errors
	FACULTY_SERVICE("Error in faculty service implementation"),
	FACULTY_ID_REQUIRED("Faculty ID is required"),
	FACULTY_REQUIRED("Faculty cannot be null or empty"),
	FACULTY_ID_NOT_FOUND("Faculty ID '%s' not found"),
	DUPLICATE_FACULTY_ID("Faculty ID '%s' already exists"),

	//Semester Service Errors
	SEMESTER_SERVICE("Error in semester service implementation"),
	SEMESTER_CODE_REQUIRED("Semester code is required"),
	SEMESTER_REQUIRED("Semester cannot be null or empty"),
	SEMESTER_CODE_NOT_FOUND("Semester code '%s' not found"),
	DUPLICATE_SEMESTER_CODE("Semester code '%s' already exists"),

	//Subject Service Errors
	SUBJECT_SERVICE("Error in subject service implementation"),
	SUBJECT_ID_REQUIRED("Subject ID is required"),
	SUBJECT_REQUIRED("Subject cannot be null or empty"),
	SUBJECT_ID_NOT_FOUND("Subject ID '%s' not found"),
	DUPLICATE_SUBJECT_CODE("Subject ID '%s' already exists"),
	SUBJECTS_NOT_FOUND("No subjects found for the given specification"),

	//User Service Errors
	USER_SERVICE("Error in user service implementation"),
	USER_ID_REQUIRED("User ID is required"),
	USER_REQUIRED("User cannot be null or empty"),
	USER_ID_NOT_FOUND("User ID '%s' not found"),
	USER_NOT_FOUND("User '%s' not found"),
	DUPLICATE_USER("User '%s' already exists"),

	//Exam Service Errors
	EXAM_NOT_FOUND("Exam record not found: '%s'"),
	EXAMS_NOT_FOUND_FOR_SPECIFICATION("Exams record not found for the given specification '%s'"),
	DUPLICATE_EXAM_CODE("Exam ID '%s' already exists"),
	FAILED_TO_SAVE_EXAM("Error occurred while saving exam"),
	FAILED_TO_UPDATE_EXAM("Error occurred while updating exam"),
	FAILED_TO_FETCH_EXAMS("Error occurred while fetching exam data"),

	//Semester-Subject Relationship Errors
	SEMESTER_SUBJECT_NOT_FOUND("Semester Subject not found: '%s'"),

	//Student-Exam Mismatch Errors
	STUDENT_EXAM_MISSMATCH("Student '%s' and Exam '%s' doesn't match"),
	STUDENT_SEMESTER_MISSMATCH("Student '%s' and Semester '%s' doesn't match"),
	DUPLICATE_STUDENT_EXAMDATA("The exam data for the given students already exists for exam '%s'"),

	//Student-Subject Mismatch Errors
	STUDENT_SUBJECT_MISSMATCH("Student '%s' is not registered for Subject '%s'"),

	//Faculty-Semester-Subject Errors
	FACULTY_SEMESTER_SUBJECTS_NOT_FOUND("Semester subjects not found for faculty");


    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
    	if (message.contains(" %s ") && (args == null || args.length == 0)) {
            return message.replaceAll(" '%s' ", "");  // This logic removes '%s' from the message, because if no argument is passed, then '%s' will be displayed as it is. 
        }
        return (args != null && args.length > 0) ? String.format(message, args) : message;
    }
}
