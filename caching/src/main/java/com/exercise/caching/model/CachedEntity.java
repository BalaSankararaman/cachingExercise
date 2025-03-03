package com.exercise.caching.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Caching Model")
public class CachedEntity implements Serializable {


	private static final long serialVersionUID = -2374575604450861648L;

	@Schema(description = "Unique identifier of the entity")
	@NotBlank(message = "ID cannot be blank")
    private String id;
    
    @Schema(description = "Data content of the entity")
    private String data;
    
    @Schema(description = "Last accessed timestamp")
    private LocalDateTime cacheAccessedTime;

    // All args constructor
    public CachedEntity(String id, String data, LocalDateTime createdAt) {
        this.id = id;
        this.data = data;
        this.cacheAccessedTime = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDateTime getCacheAccessedTime() {
        return cacheAccessedTime;
    }

    public void setCacheAccessedTime(LocalDateTime lastAccessed) {
        this.cacheAccessedTime = lastAccessed;
    }

  

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private  CachedEntity entity;

     
        public Builder id(String id) {
            entity.setId(id);
            return this;
        }

        public Builder data(String data) {
            entity.setData(data);
            return this;
        }

        public Builder cacheAccessedTime(LocalDateTime lastAccessed) {
            entity.setCacheAccessedTime(lastAccessed);
            return this;
        }

      

        public CachedEntity build() {
            return entity;
        }
    }

   

  
}


