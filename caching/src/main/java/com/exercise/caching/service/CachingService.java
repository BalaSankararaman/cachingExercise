package com.exercise.caching.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.exercise.caching.exception.CachingException;
import com.exercise.caching.exception.EntityNotFoundException;
import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.repository.CachingRepository;

import jakarta.validation.Valid;

@Service
@CacheConfig(cacheNames = {"cacheEntity"})  
public class CachingService {

	    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);
	    private final CachingRepository repository;

	    public CachingService(CachingRepository repository) {
	        this.repository = repository;
	    }

	    /**
	     * Adds an entity to the cache and database
	     * @param entity The entity to be added
	     * @return The added entity
	     * @throws CacheException if there's an error during the operation
	     * @throws IllegalArgumentException if the entity is invalid
	     */
	    @CachePut(key = "#entity.id")
	    @Transactional
	    public CachedEntity add(@Valid CachedEntity entity) {
	        try {
	            validateEntity(entity);
	            logger.debug("Adding entity to cache and database: {}", entity);

	            entity.setCacheAccessedTime(LocalDateTime.now());
	            repository.save(entity);

	            logger.info("Successfully added entity with ID: {}", entity.getId());
	            return entity;
	        } catch (IllegalArgumentException e) {
	            logger.error("Invalid entity provided: {}", e.getMessage());
	            throw e;
	        } catch (DataAccessException e) {
	            logger.error("Database error while adding entity: {}", e.getMessage());
	            throw new CachingException("Failed to add entity to database", e);
	        } catch (Exception e) {
	            logger.error("Unexpected error while adding entity: {}", e.getMessage());
	            throw new CachingException("Failed to add entity", e);
	        }
	    }

	    /**
	     * Retrieves an entity by ID from cache or database
	     * @param id The ID of the entity
	     * @return The retrieved entity
	     * @throws EntityNotFoundException if the entity is not found
	     * @throws CacheException if there's an error during the operation
	     */
	    @Cacheable(key = "#id")
	    @Transactional(readOnly = true)
	    public CachedEntity get(String id) {
	        try {
	            validateId(id);
	            logger.debug("Attempting to retrieve entity with ID: {}", id);

	            return repository.findById(id)
	                .map(entity -> {
	                    entity.setCacheAccessedTime(LocalDateTime.now());
	                    repository.save(entity);  // Update last accessed time
	                    logger.info("Successfully retrieved entity with ID: {}", id);
	                    return entity;
	                })
	                .orElseThrow(() -> {
	                    logger.warn("Entity not found with ID: {}", id);
	                    return new EntityNotFoundException("Entity not found: " + id);
	                });
	        } catch (EntityNotFoundException e) {
	            throw e;
	        } catch (DataAccessException e) {
	            logger.error("Database error while retrieving entity: {}", e.getMessage());
	            throw new CachingException("Failed to retrieve entity from database", e);
	        } catch (Exception e) {
	            logger.error("Unexpected error while retrieving entity: {}", e.getMessage());
	            throw new CachingException("Failed to retrieve entity", e);
	        }
	    }

	    /**
	     * Removes an entity from cache and database
	     * @param id The ID of the entity to remove
	     * @throws EntityNotFoundException if the entity is not found
	     * @throws CacheException if there's an error during the operation
	     */
	    @CacheEvict(key = "#id")
	    @Transactional
	    public void remove(String id) {
	        try {
	            validateId(id);
	            logger.debug("Attempting to remove entity with ID: {}", id);

	            if (!repository.existsById(id)) {
	                logger.warn("Entity not found with ID: {}", id);
	                throw new EntityNotFoundException("Entity not found: " + id);
	            }

	            repository.deleteById(id);
	            logger.info("Successfully removed entity with ID: {}", id);
	        } catch (EntityNotFoundException e) {
	            throw e;
	        } catch (DataAccessException e) {
	            logger.error("Database error while removing entity: {}", e.getMessage());
	            throw new CachingException("Failed to remove entity from database", e);
	        } catch (Exception e) {
	            logger.error("Unexpected error while removing entity: {}", e.getMessage());
	            throw new CachingException("Failed to remove entity", e);
	        }
	    }

	    /**
	     * Removes all entities from cache and database
	     * @throws CacheException if there's an error during the operation
	     */
	    @CacheEvict(allEntries = true)
	    @Transactional
	    public void removeAll() {
	        try {
	            logger.debug("Attempting to remove all entities");
	            repository.deleteAll();
	            logger.info("Successfully removed all entities");
	        } catch (DataAccessException e) {
	            logger.error("Database error while removing all entities: {}", e.getMessage());
	            throw new CachingException("Failed to remove all entities from database", e);
	        } catch (Exception e) {
	            logger.error("Unexpected error while removing all entities: {}", e.getMessage());
	            throw new CachingException("Failed to remove all entities", e);
	        }
	    }

	    /**
	     * Clears all entities from cache only
	     * @throws CacheException if there's an error during the operation
	     */
	    @CacheEvict(allEntries = true)
	    public void clear() {
	        try {
	            logger.debug("Clearing cache");
	            logger.info("Successfully cleared cache");
	        } catch (Exception e) {
	            logger.error("Unexpected error while clearing cache: {}", e.getMessage());
	            throw new CachingException("Failed to clear cache", e);
	        }
	    }

	    /**
	     * Validates an entity
	     * @param entity The entity to validate
	     * @throws IllegalArgumentException if the entity is invalid
	     */
	    private void validateEntity(CachedEntity entity) {
	        if (entity == null) {
	            throw new IllegalArgumentException("Entity cannot be null");
	        }
	        if (!StringUtils.hasText(entity.getId())) {
	            throw new IllegalArgumentException("Entity ID cannot be null or empty");
	        }
	    }

	    /**
	     * Validates an ID
	     * @param id The ID to validate
	     * @throws IllegalArgumentException if the ID is invalid
	     */
	    private void validateId(String id) {
	        if (!StringUtils.hasText(id)) {
	            throw new IllegalArgumentException("ID cannot be null or empty");
	        }
	        // Add any other ID validation rules as needed
	    }
	}
