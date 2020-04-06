package com.stories.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1002819552332825026L;
	private HttpStatus error;
	private Class<?> entityType;
	private String message;
	private Throwable cause;
	private int status;
	private String path;

	public EntityNotFoundException(String message) {
		this.error = HttpStatus.NOT_FOUND;
		this.message = message;
	}

	public EntityNotFoundException(String message, HttpStatus status, String path) {
		this.error = HttpStatus.CONFLICT;
		this.message = message;
		this.path = path;
	}

	public EntityNotFoundException(String message, String path) {
		this(message);
		this.path = path;
	}

	public EntityNotFoundException(String message, int status, Class<?> entityType, String path) {
		this.message = message;
		this.status = status;
		this.entityType = entityType;
		this.path = path;
	}

	public EntityNotFoundException(String message, String error, String path) {
		this.error = HttpStatus.BAD_REQUEST;
		this.status = HttpStatus.BAD_REQUEST.value();
		this.message = message;
		this.path = path;
	}
}