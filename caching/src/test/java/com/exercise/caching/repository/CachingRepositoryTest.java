package com.exercise.caching.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.exercise.caching.exception.DBException;
import com.exercise.caching.model.CachedEntity;

@SpringBootTest
public class CachingRepositoryTest {

	@Autowired
    private CachingRepository repository;

    @Test
    void testSaveAndFindById() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CachedEntity entity = CachedEntity.builder()
                .id("testId")
                .data("testdata")
                .cacheAccessedTime(now)
                .build();

        // When
        repository.save(entity);
        Optional<CachedEntity> found = repository.findById("testid");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testData", found.get().getData());
       
    }

    @Test
    void testFindByIdNotFound() {
        // When
        Optional<CachedEntity> found = repository.findById("non-existent");

        // Then
        assertFalse(found.isPresent());
    }



    @Test
    void testDeleteById() {
        // Given
        CachedEntity entity = CachedEntity.builder()
                .id("test-id")
                .data("test-data")
                .cacheAccessedTime(LocalDateTime.now())
                .build();
        repository.save(entity);

        // When
        repository.deleteById("test-id");

        // Then
        assertFalse(repository.findById("testId").isPresent());
    }

    @Test
    void testDeleteAll() {
        // Given
        repository.save(CachedEntity.builder()
                .id("id1")
                .data("data1")
                .cacheAccessedTime(LocalDateTime.now())
                .build());
        repository.save(CachedEntity.builder()
                .id("id2")
                .data("data2")
                .cacheAccessedTime(LocalDateTime.now())
                .build());

        // When
        repository.deleteAll();

        // Then
        assertFalse(repository.findById("id1").isPresent());
    }

    @Test
    void testSaveInvalidEntity() {
        // Given
        CachedEntity entity = CachedEntity.builder()
                .id(null)
                .build();

        // When/Then
        assertThrows(DBException.class, () -> repository.save(entity));
    }
}

