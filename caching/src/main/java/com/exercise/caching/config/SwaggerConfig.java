package com.exercise.caching.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Value("${spring.application.name:Caching Service}")
    private String applicationName;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        		.addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")))
                .info(getApiInfo());
                
                
    }

    private Info getApiInfo() {
        return new Info()
                .title(applicationName + " API Documentation")
                .version("1.0")
                .description("This is a Caching Service REST API that provides endpoints for cache operations. " +
                        "The service supports the following operations:\n\n" +
                        "* Add entities to cache\n" +
                        "* Remove entities from cache\n" +
                        "* Get entities from cache\n" +
                        "* Clear cache\n" +
                        "* Remove all entities")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .name("Cache Service Team")
                        .email("email address"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"));
    }

    

    
}

