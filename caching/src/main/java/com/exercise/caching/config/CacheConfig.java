package com.exercise.caching.config;


import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.service.EvictionHandler;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;


@Configuration
public class CacheConfig {

	@Value("${cache.max.size:100}")
    private int maxElements;
    @Value("${cache.expire.minutes:10}")
    private int expiryMinutes;

    private final EvictionHandler evictionHandler;
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    public CacheConfig(EvictionHandler evictionHandler) {
        this.evictionHandler = evictionHandler;
    }

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<Long, CachedEntity> caffeineCache() {
    	logger.info("Initializing Caffeine Cache with max size: {}", maxElements);
        
        return Caffeine.newBuilder()
                .maximumSize(maxElements)
                .expireAfterWrite(expiryMinutes, TimeUnit.MINUTES)
                .recordStats()
                .removalListener((Long key, CachedEntity value, RemovalCause cause) -> {
                    if (cause.wasEvicted()) {
                    	logger.info("Cache eviction triggered for key: {}. Cause: {}", key, cause);
                        evictionHandler.handleEviction(value);
                    }
                })
                .build();
    }
}