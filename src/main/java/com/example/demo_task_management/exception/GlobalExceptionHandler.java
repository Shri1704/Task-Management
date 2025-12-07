/*
 * Copyright 2025
 * ParaBlu Systems Private Limited
 * All Rights Reserved
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of ParaBlu Systems Private Limited and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary
 * to ParaBlu Systems Private Limited and its suppliers and may be covered by
 * Indian, US and Foreign Patents, patents in process, and are protected by
 * trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from ParaBlu Systems Private Limited.
 *
 */
package com.example.demo_task_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                System.currentTimeMillis(),ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                System.currentTimeMillis(),ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ErrorResponse> ProcessingException(ProcessingException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                System.currentTimeMillis(),ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> RuntimeException(RuntimeException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                System.currentTimeMillis(),"Please contact the administrator",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
