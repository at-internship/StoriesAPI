package com.stories.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "stories")
public class TaskModel {

	@Id
	private String _id;
	private String name;
	private String description;
	private String status;
	private String comments;
	private String assignee;
	
}