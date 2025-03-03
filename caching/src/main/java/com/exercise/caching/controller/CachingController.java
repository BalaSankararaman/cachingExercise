package com.exercise.caching.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.caching.exception.EntityNotFoundException;
import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.model.ErrorResponse;
import com.exercise.caching.service.CachingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/caching")
@Tag(name = "Caching Controller", description = "Cache management endpoints")
@Validated
public class CachingController {
	
	private static final Logger logger = LoggerFactory.getLogger(CachingController.class);
    private final CachingService cachingService;

    public CachingController(CachingService cacheService) {
        this.cachingService = cacheService;
    }

    @PostMapping
    @Operation(summary = "Add entity to cache")
    @ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", 
    	                description = "Entity added successfully",
    	                content = @Content(schema = @Schema(implementation = CachedEntity.class))),
    	    @ApiResponse(responseCode = "400", 
    	                description = "Invalid input",
    	                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    	    @ApiResponse(responseCode = "401", description = "Unauthorized",
    	    			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    	    @ApiResponse(responseCode = "500", 
    	                description = "Internal server error",
    	                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    	})
    public ResponseEntity<?> add(@Parameter(description = "Entity to be added", required = true)
    							 @Valid @RequestBody CachedEntity entity) {
        logger.debug("REST API call to add entity: {}", entity);
        try {
           
            CachedEntity addedEntity = cachingService.add(entity);
            logger.info("Successfully added entity with ID: {}", entity.getId());
            return ResponseEntity.ok(addedEntity);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for add entity request", e);
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid input: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding entity", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding entity");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by ID")
    @ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", 
    	                description = "Entity retrieved successfully",
    	                content = @Content(schema = @Schema(implementation = CachedEntity.class))),
    	    @ApiResponse(responseCode = "400", 
    	                description = "Invalid input",
    	                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    	    @ApiResponse(responseCode = "500", 
    	                description = "Internal server error",
    	                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    	})
    public ResponseEntity<?> get(
            @Parameter(description = "ID of the entity", required = true)
            @PathVariable @NotBlank String id) {
        try {
            logger.debug("Received request to get entity with ID: {}", id);
            CachedEntity entity = cachingService.get(id);
            logger.info("Successfully retrieved entity with ID: {}", id);
            return ResponseEntity.ok(entity);
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found with ID: {}", id);
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            logger.error("Error retrieving entity with ID: {}", id, e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error retrieving entity");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove entity", description = "Removes an entity from cache and database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity removed successfully"),
        @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> remove(
            @Parameter(description = "ID of the entity to remove", required = true)
            @PathVariable @NotBlank String id) {
        try {
            logger.debug("Received request to remove entity with ID: {}", id);
            cachingService.remove(id);
            logger.info("Successfully removed entity with ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found with ID: {}", id);
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            logger.error("Error removing entity with ID: {}", id, e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error removing entity");
        }
    }

    @DeleteMapping
    @Operation(summary = "Remove all entities", description = "Removes all entities from cache and database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All entities removed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> removeAll() {
        try {
            logger.debug("Received request to remove all entities");
            cachingService.removeAll();
            logger.info("Successfully removed all entities");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error removing all entities", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error removing all entities");
        }
    }

    @PostMapping("/clear")
    @Operation(summary = "Clear cache", description = "Clears all entities from cache only")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cache cleared successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> clear() {
        try {
            logger.debug("Received request to clear cache");
            cachingService.clear();
            logger.info("Successfully cleared cache");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error clearing cache", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error clearing cache");
        }
    }
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse =  new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }
}


