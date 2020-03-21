package com.stories.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

@Data
public class StoryDomain {

	private String sprint_id;
	private String technology;
	@Indexed(unique = true)
	@NotBlank(message = "name is required")
	private String name;
	private String description;
	private String acceptance_criteria;
	@Min(1)
	private int points;
	@Min(1)
	private int progress;
	@NotBlank(message = "Status is required")
	private String status;
	private String notes;
	private String comments;
	private LocalDate start_date;
	private LocalDate due_date;
	private String priority;
	private String assignee_id;
	private List<String> history;

	public StoryDomain() {
		this.history = new ArrayList<>();
	}
}
