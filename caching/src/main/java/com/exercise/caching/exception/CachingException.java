package com.exercise.caching.exception;

public class CachingException extends RuntimeException {
	
    private static final long serialVersionUID = 9111398992424709171L;

	public CachingException(String message) {
        super(message);
    }

    public CachingException(String message, Throwable cause) {
        super(message, cause);
    }
}
