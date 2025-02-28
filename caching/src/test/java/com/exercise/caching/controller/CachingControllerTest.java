package com.exercise.caching.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import org.springframework.test.web.servlet.MockMvc;

import com.exercise.caching.exception.EntityNotFoundException;
import com.exercise.caching.model.CachedEntity;
import com.exercise.caching.service.CachingService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class CachingControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@MockitoSpyBean
    private CachingService cacheService;

    @Autowired
    private ObjectMapper objectMapper;

    private CachedEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = CachedEntity.builder()
                .id("test-id")
                .data("test-data")
                .cacheAccessedTime(LocalDateTime.now())
                .build();
    }

    @Test
    void whenAddEntity_thenSuccess() throws Exception {
        // Given
        doReturn(testEntity).when(cacheService).add(any(CachedEntity.class));

        // When & Then
        mockMvc.perform(post("/api/cache")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"))
                .andExpect(jsonPath("$.data").value("test-data"));

        verify(cacheService).add(any(CachedEntity.class));
    }

    @Test
    void whenGetEntity_thenSuccess() throws Exception {
        // Given
        doReturn(testEntity).when(cacheService).get(anyString());

        // When & Then
        mockMvc.perform(get("/api/cache/{id}", "test-id"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"))
                .andExpect(jsonPath("$.data").value("test-data"));

        verify(cacheService).get("test-id");
    }

    @Test
    void whenGetNonExistentEntity_thenNotFound() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Entity not found: test-id"))
            .when(cacheService).get(anyString());

        // When & Then
        mockMvc.perform(get("/api/cache/{id}", "test-id"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity not found: test-id"));
    }

    @Test
    void whenRemoveEntity_thenSuccess() throws Exception {
        // Given
        doNothing().when(cacheService).remove(anyString());

        // When & Then
        mockMvc.perform(delete("/api/cache/{id}", "test-id"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(cacheService).remove("test-id");
    }

    @Test
    void whenRemoveAll_thenSuccess() throws Exception {
        // Given
        doNothing().when(cacheService).removeAll();

        // When & Then
        mockMvc.perform(delete("/api/cache"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(cacheService).removeAll();
    }

    @Test
    void whenClearCache_thenSuccess() throws Exception {
        // Given
        doNothing().when(cacheService).clear();

        // When & Then
        mockMvc.perform(post("/api/cache/clear"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(cacheService).clear();
    }

    @Test
    void whenAddInvalidEntity_thenBadRequest() throws Exception {
        // Given
        CachedEntity invalidEntity = CachedEntity.builder().build();

        // When & Then
        mockMvc.perform(post("/api/cache")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddNullEntity_thenBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/cache")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetWithInvalidId_thenBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/cache/{id}", " "))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_thenInternalServerError() throws Exception {
        // Given
        doThrow(new RuntimeException("Service failure"))
            .when(cacheService).add(any(CachedEntity.class));

        // When & Then
        mockMvc.perform(post("/api/cache")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEntity)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void whenInvalidContentType_thenUnsupportedMediaType() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/cache")
                .content(objectMapper.writeValueAsString(testEntity)))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void whenInvalidMethod_thenMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/cache")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEntity)))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
}


