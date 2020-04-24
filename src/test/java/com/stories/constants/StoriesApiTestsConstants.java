package com.stories.constants;


public class StoriesApiTestsConstants {
	
	public static final String uriStories = "/stories/";
	public static final String uriGetByIdInvalid = "/stories/5e6a8441bf#ERFSasda";
	public static final String uriStory = "/stories/5e7134c9099a9a0ab248c90b";
	public static final String uriTask = "/stories/5e7133b6430bf4151ec1e85f/tasks/5e7133b6430bf4151ec1e85f";
	public static final String uriTaskInvalid = "/stories/5e7133b6430bf4151ec1e85f/tasks/5e6a8441bf#ERFSasda";
	public static final String uriTasks = "/stories/5e7133b6430bf4151ec1e85f/tasks/";
	
	public static final String idValid = "5e7134c9099a9a0ab248c90b";
	public static final String ValidPutTaskId = "5e8cf37b7a605837de2865ad";
	
	public static final String InvalidId = "wrong123";
	
	public static final Boolean booleanTrue = true;
	public static final Boolean booleanFalse = false;
	
	public static final String messageTask = "Task with the given id was not found.";
	public static final String messageIdTask = "Task with the given id was not found.";
	public static final String messageMalformedJSON = "Task with the given id was not found.";
	public static final String messageStoryJson = "Story has an invalid status Json";
	public static final String messageStory = "Story not found";
	public static final String messageStories = "Stories not found";
	public static final String messageName = "The JSON format provided is invalid, please provide the required field ('Name').";
	public static final String messageSprintId = "The sprint_id does not exists";
	public static final String messageStatusInvalid = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.";
	public static final String numbreError = "400";
	public static final String path = "/stories/";
	public static final String pathTask = "/tasks/";
	public static final String plusError = "fdsfd";
	public static final String varEmpty = "";
	
	public static final String uriSprintClient = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/";
	public static final String sprintIdValid = "5e827f2f48b0866f87e1cbc2";
	public static final String sprintIdInvalid = "5e78f5e792675632e42d1a96";
	public static final String messageSprints = "sprints API has no entities";
	
	public static final String specificId = "5e8dc1ba4ce33c0efc555845";
}
