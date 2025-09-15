package com.sts.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.sts.constants.ErrorResponse;
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;

//public class ResponseBuilder {
//
//    private ResponseBuilder() {}
//
//    // Success Responses
//    public static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(T data, SuccessMessages errorMessage, HttpStatus status, Object... successMessageArgs) {
//        SuccessResponse<T> response = new SuccessResponse<>(
//                data,
//                errorMessage.getMessage(successMessageArgs),
//                status.value()
//        );
//        return ResponseEntity.status(status).body(response);
//    }
//
//    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data, SuccessMessages messageEnum, Object... args) {
//        return buildSuccessResponse(data, messageEnum, HttpStatus.OK, args);
//    }
//
//    public static <T> ResponseEntity<SuccessResponse<T>> created(T data, SuccessMessages messageEnum, Object... args) {
//        return buildSuccessResponse(data, messageEnum, HttpStatus.CREATED, args);
//    }
//
//    // Error Responses
//    public static ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                status.value(),
//                status.getReasonPhrase(),
//                message,
//                request.getDescription(false)
//        );
//        return ResponseEntity.status(status).body(errorResponse);
//    }
//}

public class ResponseBuilder {

    private ResponseBuilder() {}

    // Success Responses
    public static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(HttpStatus status, T data, SuccessMessages messageEnum, Object... successMessageArgs) {
        SuccessResponse<T> response = new SuccessResponse<>(
                data,
                messageEnum.getMessage(successMessageArgs),
                status.value()
        );
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data, SuccessMessages messageEnum, Object... args) {
        return buildSuccessResponse(HttpStatus.OK, data, messageEnum, args);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(T data, SuccessMessages messageEnum, Object... args) {
        return buildSuccessResponse(HttpStatus.CREATED, data, messageEnum, args);
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
