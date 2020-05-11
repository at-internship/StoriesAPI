package com.stories.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StoryDomain {
	
	@ApiModelProperty(example="5e827f2f48b0866f87e1cbc2", value="Identifier of the story")
	private String _id;
	
	@ApiModelProperty(example="5ea1cabbcafedd13f03bf05c", value="Identifier of the sprint")
	private String sprint_id;
	
	@ApiModelProperty(example="Java", value="Technology used")
	private String technology;
	
	@ApiModelProperty(example="Create new story", value="Name of the story")
	@Indexed(unique = true)
	private String name;
	
	@ApiModelProperty(example="Make new Stories", value="Story description")
	private String description;
	
	@ApiModelProperty(example="As a website user, I want to able to submit feedback.", value= "Acceptance criteria of the story")
	private String acceptance_criteria;
	
	@ApiModelProperty(example="1", value="Points of the story")
	private int points;
	
	@ApiModelProperty(example="10", value="Progress of the story")
	private int progress;
	
	@ApiModelProperty(example="Working", value="Status of the story")
	private String status;
	
	@ApiModelProperty(example="The first steps", value="Notes of the story")
	private String notes;
	
	@ApiModelProperty(example="Research information in sure websites", value="Comments of the story")
	private String comments;
	
	@ApiModelProperty(example="2020-08-25", value="Start date of the story")
	private LocalDate start_date;
	
	@ApiModelProperty(example="2020-08-25", value="Due date of the story")
	private LocalDate due_date;
	
	@ApiModelProperty(example="High", value="Priority of the story")
	private String priority;
	
	@ApiModelProperty(example="5ea7125ce6cd3109e8bc71c6", value="Identifier of the user")
	private String assignee_id;
	
	@ApiModelProperty(example="[]", value="The history of the story")
	private List<String> history;
	
	@ApiModelProperty(example="[]", value="The tasks of the story")
	private List<TasksDomain> tasks;

	public StoryDomain() {
		this.history = new ArrayList<>();
		this.tasks = new ArrayList<>();
	}
}
