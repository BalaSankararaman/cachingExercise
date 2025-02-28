package com.exercise.caching.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void testErrorResponseBuilder() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String message = "Entity not found";
        String path = "/api/cache/123";

        // When
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        // Then
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
    }

    @Test
    void testErrorResponseBuilderWithNullValues() {
        // When
        ErrorResponse errorResponse = ErrorResponse.builder()
                .build();

        // Then
        assertNotNull(errorResponse);
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
    }

   

   

    @Test
    void testBuilderChaining() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();

        // When
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path("/api/cache")
                .build();

        // Then
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/api/cache", errorResponse.getPath());
    }



    @Test
    void testDifferentErrorResponses() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse error1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .message("Entity not found")
                .path("/api/cache/123")
                .build();

        ErrorResponse error2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Internal Server Error")
                .message("System error")
                .path("/api/cache/456")
                .build();

        // Then
        assertNotEquals(error1, error2);
        assertNotEquals(error1.hashCode(), error2.hashCode());
    }

}