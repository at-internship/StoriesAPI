package com.stories.domain;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class StoryDomain {
	
	@ApiModelProperty(example="1", value="Identifier of the sprint")
	private String sprint_id;
	
	@ApiModelProperty(example="Java", value="Technology used")
	private String technology;
	
	@ApiModelProperty(example="Create new story", value="Name of the story")
	@Indexed(unique = true)
	@NotBlank(message = "name is required")
	private String name;
	
	@ApiModelProperty(example="Make new Stories", value="Story description")
	private String description;
	
	@ApiModelProperty(example="1", value="Acceptance criteria of the story")
	private String acceptance_criteria;
	
	@ApiModelProperty(example="1", value="Points of the story")
	@Min(1)
	private int points;
	
	@ApiModelProperty(example="1", value="Progress of the story")
	@Min(1)
	private int progress;
	
	@ApiModelProperty(example="Working", value="Status of the story")
	@NotBlank(message = "Status is required")
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
	
	@ApiModelProperty(example="1", value="Assignee id of the story")
	private String assignee_id;
	
	@ApiModelProperty(example="1", value="The history of the story")
	private List<String> history;

	public StoryDomain() {
		this.history = new ArrayList<>();
	}
}
