package com.exercise.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
    info = @Info(
        title = "Caching API",
        version = "1.0",
        description = "REST API for Caching with Caffeine Cache"
    )
)
public class CachingApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(CachingApplication.class);
	public static void main(String[] args) {
		LOGGER.info("Starting Caching application");
		SpringApplication.run(CachingApplication.class, args);
	}

}
