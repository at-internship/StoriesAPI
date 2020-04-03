package com.stories.exception;

import org.springframework.http.HttpStatus;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;

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
	private int code;

	public EntityNotFoundException(String message) {
		this.status = HttpStatus.NOT_FOUND;
		this.message = message;
	}
	
	public EntityNotFoundException(String message, HttpStatus status, Class<StoryModel> entityType) {
        this.status = HttpStatus.CONFLICT;
        this.message = message;
        this.entityType = entityType;
    }

	public EntityNotFoundException(String message, Class<?> entityType) {
		this(message);
		this.entityType = entityType;
	}
	
	 public EntityNotFoundException(String message, int code, Class<?> entityType) {
	        this.message = message;
	        this.code = code;
	        this.entityType = entityType;
	    }

	public EntityNotFoundException(String message, int code, String status, Class<StoryDomain> entityType) {
		this.status = HttpStatus.BAD_REQUEST;
		this.code = code;
		this.message = message;
		this.entityType = entityType;
	}
}