package com.exercise.caching.exception;

public class EntityNotFoundException extends CachingException {
    private static final long serialVersionUID = -8807777232867752942L;

	public EntityNotFoundException(String message) {
        super(message);
    }
}
