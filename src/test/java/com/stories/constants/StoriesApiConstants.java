package com.stories.constants;

import lombok.Data;

@Data
public class StoriesApiConstants {
	
	final String uriStories = "/stories/";
	final String uriGetByIdInvalid = "/stories/5e6a8441bf#ERFSasda";
	final String uriStory = "/stories/5e7134c9099a9a0ab248c90b";
	final String uriTask = "/stories/5e7133b6430bf4151ec1e85f/tasks/5e7133b6430bf4151ec1e85f";
	final String uriTaskInvalid = "/stories/5e7133b6430bf4151ec1e85f/tasks/5e6a8441bf#ERFSasda";
	final String uriTasks = "/stories/5e7133b6430bf4151ec1e85f/tasks/";
	
	final String idValid = "5e7134c9099a9a0ab248c90b";
	
	final Boolean booleanTrue = true;
	final Boolean booleanFalse = false;
	
	final String messageTask = "Task with the given id was not found.";
	final String messageIdTask = "Task with the given id was not found.";
	final String messageMalformedJSON = "Task with the given id was not found.";
	final String messageStoryJson = "Story has an invalid status Json";
	final String messageStory = "Story not found";
	final String messageStories = "Stories not found";
	final String messageName = "The JSON format provided is invalid, please provide the required field ('Name').";
	final String messageSprintId = "The sprint_id does not exists";
	final String messageStatusInvalid = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.";
	final String numbreError = "400";
	final String path = "/stories/";
	final String pathTask = "/tasks/";
	final String plusError = "fdsfd";
	final String varEmpty = "";
	
	final String uriSprintClient = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
	final String sprintIdValid = "5e827f2f48b0866f87e1cbc2";
	final String sprintIdInvalid = "5e78f5e792675632e42d1a96";
	final String messageSprints = "sprints API has no entities";
}
