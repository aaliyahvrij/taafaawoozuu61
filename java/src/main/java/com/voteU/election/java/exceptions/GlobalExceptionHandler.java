package com.voteU.election.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


    @RestControllerAdvice
    public class GlobalExceptionHandler {

        // IllegalArgumentException -> 400 Bad Request
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

        // IllegalStateException -> 409 Conflict (of 400 Bad Request, afhankelijk van use case)
        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

        // ResourceNotFoundException -> 404 Not Found
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }


        // Algemene fallback voor onverwachte errors
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Er is een fout opgetreden.");
        }
    }


