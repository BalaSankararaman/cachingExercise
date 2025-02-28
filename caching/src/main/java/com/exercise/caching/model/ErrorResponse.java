package com.exercise.caching.model;

import java.time.LocalDateTime;

import lombok.Data;



@Data
public class ErrorResponse {
    public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Private constructor for builder pattern
    private ErrorResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    // Static Builder class
    public static class Builder {
        private final ErrorResponse errorResponse;

        private Builder() {
            this.errorResponse = new ErrorResponse();
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.errorResponse.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.errorResponse.status = status;
            return this;
        }

        public Builder error(String error) {
            this.errorResponse.error = error;
            return this;
        }

        public Builder message(String message) {
            this.errorResponse.message = message;
            return this;
        }

        public Builder path(String path) {
            this.errorResponse.path = path;
            return this;
        }

        public ErrorResponse build() {
            return this.errorResponse;
        }
    }
}
