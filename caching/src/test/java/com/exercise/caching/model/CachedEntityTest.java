package com.exercise.caching.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class CachedEntityTest {

	@Test
    void testBuilderPattern() {
        LocalDateTime now = LocalDateTime.now();
        CachedEntity entity = CachedEntity.builder()
                .id("test-id")
                .data("test-data")
                .cacheAccessedTime(now)
                .build();

        assertEquals("test-id", entity.getId());
        assertEquals("test-data", entity.getData());
        assertEquals(now, entity.getCacheAccessedTime());
       
    }

    @Test
    void testConstructor() {
        LocalDateTime now = LocalDateTime.now();
        CachedEntity entity = new CachedEntity("test-id", "test-data", now);

        assertEquals("test-id", entity.getId());
        assertEquals("test-data", entity.getData());
        assertEquals(now, entity.getCacheAccessedTime());
       
    }

}
