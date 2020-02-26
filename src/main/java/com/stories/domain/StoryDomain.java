package com.stories.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StoryDomain {

	private String sprint_id;
	private String technology;
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
