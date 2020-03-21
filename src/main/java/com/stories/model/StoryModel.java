package com.stories.model;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@Pattern(regexp = "\\A(?!\\s*\\Z).+")
	@Size(min = 1, message = "This field must contain something")
	@NotNull(message = "This field must be not null")
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
