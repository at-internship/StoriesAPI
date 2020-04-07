package com.stories.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TasksDomain {

	@ApiModelProperty(example="5e827f2f48b0866f87e1cbc2", value="Identifier of the task")
	private String _id;
	
	@ApiModelProperty(example="Make PR", value="name of the task")
	private String name;
	
	@ApiModelProperty(example="Create Pull Request to master", value="description of the task")
	private String description;
	
	@ApiModelProperty(example="Done", value="status of the task")
	private String status;
	
	@ApiModelProperty(example="Waiting for review", value="comments about the task")
	private String comments;
	
	@ApiModelProperty(example="John Wick", value="name of assignee of the task")
	private String assignee;
	
}