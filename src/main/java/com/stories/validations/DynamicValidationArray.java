package com.stories.validations;

import java.time.LocalDate;
import java.util.ArrayList;

import com.stories.domain.SprintDomain;

import lombok.Data;

@Data
public class DynamicValidationArray {
	
	private String message;
	private String status;
	private String path;
	
	
	public DynamicValidationArray() {
		this.setMessage("");
		this.setStatus("");
		this.setPath("");
	}
}
