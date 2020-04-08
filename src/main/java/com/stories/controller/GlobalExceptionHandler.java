package com.stories.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stories.exception.ApiError;
import com.stories.exception.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String[] statusArray = { "points", "progress", "start_date", "due_date" };
		String mss = "";
		for (int i = 0; i < statusArray.length; i++) {
			if (ex.toString().indexOf(statusArray[i]) == -1) {

			} else {
				if (statusArray[i].equals(ex.toString().substring(ex.toString().indexOf(statusArray[i]),
						ex.toString().indexOf(statusArray[i]) + statusArray[i].length()))) {
					mss = "";
					mss = ex.toString().substring(ex.toString().indexOf(statusArray[i]),
							ex.toString().indexOf(statusArray[i]) + statusArray[i].length());
					if (mss.equals("start_date") || mss.equals("due_date")) {
						mss = "Malformed JSON request, format date should be: ex. 'YYYY-MM-DD'; at " + mss;
					} 
					else if(mss.equals("points")) {
						mss = "Malformed JSON request, The format of the "+ mss +" field is not valid, Non-numeric content";
					}
					else if(mss.equals("progress")) {
						mss = "Malformed JSON request, The format of the "+ mss +" field is not valid, Non-numeric content";
					}
					else {
						mss = "Malformed JSON request " + mss;
					}
					return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, 400, mss, "/stories/"));
				}
			}
		}
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getError());
	}

	@ExceptionHandler({ EntityNotFoundException.class })
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
		return buildResponseEntity(
				new ApiError(ex.getError(), ex.getStatus(), ex.getMessage(), ex.getPath().toString()));
	}
}