package com.exercise.caching.exception;

public class DBException extends CachingException {
    private static final long serialVersionUID = 3385645707523781693L;

	public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
