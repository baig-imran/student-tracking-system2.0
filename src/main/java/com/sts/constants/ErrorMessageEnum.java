package com.sts.constants;


public enum ErrorMessageEnum {
    
    ACCESS_DENIED("Access is denied."),
    BAD_REQUEST("Invalid request parameters."),
    INTERNAL_ERROR("An unexpected error occurred."),
    DATABASE_ERROR("Database operation failed."),
    DUPLICATE_RESOURCE("Resource already exists."),
    EXTERNAL_SERVICE_ERROR("External service error."),
    INVALID_INPUT("Invalid input provided."),
    RESOURCE_NOT_FOUND("Requested resource not found."),
    UNAUTHORIZED_ACCESS("Unauthorized access."),
    VALIDATION_FAILED("Validation failed.");

    private final String message;

    ErrorMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

