package com.exercise.caching.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.repository.CachingRepository;

@SpringBootTest
public class CachingServiceTest {

	@Mock
    private CachingRepository repository;

    @Mock
    private CacheManager cacheManager;

    private CachingService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CachingService(repository);
    }


    @Test
    void testGetExistingEntity() {
        // Given
        String id = "test-id";
        CachedEntity entity = CachedEntity.builder()
                .id(id)
                .data("test-data")
                .cacheAccessedTime(LocalDateTime.now())
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        // When
        CachedEntity found = cacheService.get(id);

        // Then
        assertNotNull(found);
        assertEquals(id, found.getId());
    }
}
