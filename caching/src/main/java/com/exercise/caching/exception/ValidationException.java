package com.exercise.caching.exception;

public class ValidationException extends CachingException {
    private static final long serialVersionUID = 7599568046186653170L;

	public ValidationException(String message) {
        super(message);
    }

}
