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
	
	@ApiModelProperty(example="1", value="acceptance_criteria of the story")
	private String acceptance_criteria;
	
	@ApiModelProperty(example="1", value="points of the story")
	@Min(1)
	private int points;
	
	@ApiModelProperty(example="1", value="progress of the story")
	@Min(1)
	private int progress;
	
	@ApiModelProperty(example="Working", value="status of the story")
	@NotBlank(message = "Status is required")
	private String status;
	
	@ApiModelProperty(example="The first steps", value="notes of the story")
	private String notes;
	
	@ApiModelProperty(example="That do you don't", value="comments of the story")
	private String comments;
	
	@ApiModelProperty(example="2020-08-25", value="start_date of the story")
	private LocalDate start_date;
	
	@ApiModelProperty(example="2020-08-25", value="due_date of the story")
	private LocalDate due_date;
	
	@ApiModelProperty(example="High", value="priority of the story")
	private String priority;
	
	@ApiModelProperty(example="1", value="assignee_id of the story")
	private String assignee_id;
	
	@ApiModelProperty(example="1", value="the history of the story")
	private List<String> history;

	public StoryDomain() {
		this.history = new ArrayList<>();
	}
}
