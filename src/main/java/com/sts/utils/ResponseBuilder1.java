package com.sts.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.sts.constants.ErrorResponse;
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;

public class ResponseBuilder1 {

    private ResponseBuilder1() {}

    // Success Responses
    public static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(HttpStatus status, String message, T data) {
        SuccessResponse<T> response = new SuccessResponse<>(
                data,
                message,
                status.value()
        );
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> ok(String message, T data) {
        return buildSuccessResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(String message, T data) {
        return buildSuccessResponse(HttpStatus.CREATED, message, data);
    }

    // Error Responses
    public static ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false)
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}