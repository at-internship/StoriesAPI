package com.stories.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1002819552332825026L;
	private HttpStatus status;
	private Class<?> entityType;
	private String message;
	private Throwable cause;

	public EntityNotFoundException(String message) {
		this.status = HttpStatus.NOT_FOUND;
		this.message = message;
	}

	public EntityNotFoundException(String message, Class<?> entityType) {
		this(message);
		this.entityType = entityType;
	}
}