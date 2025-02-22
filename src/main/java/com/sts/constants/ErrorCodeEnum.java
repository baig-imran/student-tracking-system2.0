package com.sts.constants;

public enum ErrorCodeEnum {
    UNKNOWN_EXCEPTION("Unknown Error"),
    CUSTOM_EXCEPTION(""),

    STUDENT_SERVICE("Error in student service implementation"),
    STUDENT_ID_REQUIRED("Student ID cannot be null or empty"),
    STUDENT_REQUIRED("Student cannot be null or empty"),
    STUDENT_ID_NOT_FOUND("Student ID does not exist"),
    DUPLICATE_STUDENT_ID("Student with the given ID already exists"),

    DEPARTMENT_SERVICE("Error in department service implementation"),
    DEPARTMENT_ID_REQUIRED("Department ID cannot be null or empty"),
    DEPARTMENT_ID_NOT_FOUND("Department ID does not exist"),
    DUPLICATE_DEPARTMENT_ID("Department with the given ID already exists"),
    DEPARTMENT_REQUIRED("Department cannot be null or empty"),

    FACULTY_SERVICE("Error in faculty service implementation"),
    FACULTY_ID_REQUIRED("Faculty ID is required and cannot be null or empty"),
    FACULTY_ID_NOT_FOUND("Faculty ID does not exist"),
    DUPLICATE_FACULTY_ID("Faculty with the given ID already exists"),
    FACULTY_REQUIRED("Faculty cannot be null or empty"),

    SEMESTER_SERVICE("Error in semester service implementation"),
    SEMESTER_CODE_REQUIRED("Semester code is required and cannot be null or empty"),
    SEMESTER_CODE_NOT_FOUND("Semester code does not exist"),
    DUPLICATE_SEMESTER_CODE("Semester with the given code already exists"),
    SEMESTER_REQUIRED("Semester cannot be null or empty"),

    SUBJECT_SERVICE("Error in subject service implementation"),
    SUBJECT_CODE_REQUIRED("Subject code is required and cannot be null or empty"),
    SUBJECT_CODE_NOT_FOUND("Subject code does not exist"),
    SUBJECT_REQUIRED("Subject cannot be null or empty"),
    DUPLICATE_SUBJECT_CODE("Subject with the given code already exists");

    private final String errorMessage;

    ErrorCodeEnum(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
   
}
