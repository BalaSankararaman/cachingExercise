package com.exercise.caching.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void testErrorResponseBuilder() {
        // Given
       
        int status = 404;
        String message = "Entity not found";
        

        // When
        ErrorResponse errorResponse = new ErrorResponse(status, message);
                

        // Then
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
      
        assertEquals(message, errorResponse.getMessage());

    }

    @Test
    void testErrorResponseBuilderWithNullValues() {
        // When
        ErrorResponse errorResponse = new ErrorResponse(0,null);
               

        // Then
        assertNotNull(errorResponse);
        assertEquals(0, errorResponse.getStatus());
     
        assertNull(errorResponse.getMessage());
        
    }

   


}