package com.exercise.caching.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.repository.CachingRepository;



@Component
public class EvictionHandler {
    
	private static final Logger logger = LoggerFactory.getLogger(EvictionHandler.class);
    private final CachingRepository repository;

    public EvictionHandler(CachingRepository repository) {
        this.repository = repository;
    }

    public void handleEviction(CachedEntity entity) {
        try {
            if (entity != null) {
            	logger.info("Handling eviction for entity ID: {}", entity.getId());
                repository.save(entity);
                logger.debug("Successfully persisted evicted entity to database: {}", entity);
            }
        } catch (Exception e) {
        	logger.error("Failed to handle eviction for entity: {}. Error: {}", 
                entity != null ? entity.getId() : "null", e.getMessage(), e);
        }
    }

}
