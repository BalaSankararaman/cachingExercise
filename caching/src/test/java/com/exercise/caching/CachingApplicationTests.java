package com.exercise.caching;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CachingApplicationTests {

	@Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CaffeineCacheManager caffeineCacheManager;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void cacheManagerIsConfigured() {
        assertNotNull(caffeineCacheManager);
        assertTrue(caffeineCacheManager.getCacheNames().contains("cacheEntity"));
    }

    @Test
    void applicationStartsWithoutErrors() {
        CachingApplication.main(new String[]{});
    }

    @Test
    void requiredBeansArePresent() {
        // Check if essential beans are present in the context
       
        assertTrue(applicationContext.containsBean("jdbcTemplate"));
    }

    @Test
    void cacheIsInitiallyEmpty() {
        assertNotNull(caffeineCacheManager.getCache("cacheEntity"));
        assertNull(caffeineCacheManager.getCache("cacheEntity").get("id1"));
    }
}

