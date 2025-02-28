package com.exercise.caching.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${cache.maxElements:100}")
    private int maxElements;
    @Value("${cache.expiryMinutes:10}")
    private int expiryMinutes;


    @Bean
    CacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("cacheEntity");
        caffeineCacheManager.setCaffeine(caffeineCacheBuilder());
        logger.info("Initializing Cache Manager with maxElements={}, expiryMinutes={}", 
	            maxElements, expiryMinutes);
        return caffeineCacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
    	 
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(maxElements)
                .expireAfterWrite(Duration.ofMinutes(expiryMinutes));
    }
   
}