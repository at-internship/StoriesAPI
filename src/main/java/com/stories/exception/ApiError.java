package com.stories.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String error;
	private int status;
	private String message;
	private String path;

	public ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus error) {
		this();
		this.error = error.getReasonPhrase();
	}

	public ApiError(HttpStatus error, Throwable ex) {
		this();
		this.error = error.getReasonPhrase();
		this.message = "Unexpected error";
	}

	public ApiError(HttpStatus error, String message) {
		this();
		this.error = error.getReasonPhrase();
		this.message = message;
	}

	public ApiError(HttpStatus error, String message, Throwable ex) {
		this();
		this.error = error.getReasonPhrase();
		this.message = message;
	}

	public ApiError(HttpStatus error, int status, String message, String path) {
		this();
		this.error = error.getReasonPhrase();
		this.status = error.value();
		this.message = message;
		this.path = path;
	}

	public ApiError(HttpStatus error, String message, Throwable ex, String path) {
		this();
		this.error = error.getReasonPhrase();
		this.message = message;
		this.path = path;
	}
}