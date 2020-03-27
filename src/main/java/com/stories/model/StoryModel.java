package com.stories.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "stories")
public class StoryModel {

	@Id
	private String _id;
	private String sprint_id;
	private String technology;
	@Indexed(unique = true)
	private String name;
	private String description;
	private String acceptance_criteria;
	private int points;
	private int progress;
	private String status;
	private String notes;
	private String comments;
	private LocalDate start_date;
	private LocalDate due_date;
	private String priority;
	private String assignee_id;
	private List<String> history;

}
