package com.stories.constants;

public class StoriesApiConstants {
		public static final String[] statusArray = { "Refining", "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		public static final String[] storyDomainValidationArray = { "Story_id", "Sprint_id", "Status", "Assignee_id" };
		public static final String[] taskDomainValidationArray = { "Story_id", "Task_id", "Status", "assignee" };
		public static final int[] pointsArray = { 0, 1, 2, 3, 5 };
		public static final String[] specialCharactersArray = {"?","!","(",")","$","&","/","@","^",">","<","|","#",";",":","[","]","{","}","+","*","¨","¿","-","=","%",",","'",".","_","°","~"};
		public static final String[] validationPathArray = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };
		
		public static final String httpStatusBadRequest = "BAD_REQUEST";
		public static final String httpStatusConflict = "CONFLICT";
		public static final String httpCodeStatusBadRequest = "400";

		public static final String pathStories = "/stories/";
		public static final String pathTasks = "/tasks/";
		
		
		public static final String storyFieldStatusInvalidException = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'";
		public static final String storyFieldsNameAndStatusRequiredException = "The JSON format provided is invalid, please provide the required fields ('Name','Status').";
		public static final String storyFieldNameRequiredException = "The JSON format provided is invalid, please provide the required field ('Name').";
		public static final String storyFieldStatusRequiredException = "The JSON format provided is invalid, please provide the required field ('Status').";
		public static final String storyFieldNameExistException = "There is a story with this name already.";
		public static final String storyFieldTasksEmptyException = "There are not tasks for this user story yet.";
		public static final String storiesNotFoundException = "Stories not found";
		public static final String storyFieldIdNotFoundException = "Story with the given id was not found.";
		public static final String storyFieldSprintIdDoesntExistException = "The id entered in the sprint_id field does not exist";
		public static final String storyFieldAssigneDoesntExistException = "User assignee_id does not exist";
		public static final String storyFieldStoryIdSpecialCharacter = "Story not found";
		public static final String storyFieldSprintIdSpecialCharacter = "The id entered in the sprint_id field does not exist";
		public static final String storyFieldAssigneSpecialCharacter = "User assignee_id does not exist";
		public static final String storyFieldProgressNegativeException = "The number entered in the progress field is a negative number";
		public static final String storyFieldProgressExceedsException = "The number entered in the progress field exceeds 100%";
		public static final String storyFieldPointsNegativeException = "The number entered in the points field is a negative number";
		public static final String storyFieldPointsInvalidException  = "The number entered in the points field does not match a valid story point";
		
		public static final String taskFieldAssigneeNotFoundException = "User assignee does not exist";
		public static final String taskFieldIdNotFoundException = "Task with the given id was not found.";
		public static final String taskFieldNameRequiredException = "The JSON format provided is invalid, please provide the required field ('Name').";
		public static final String storyFieldTaskIdSpecialCharacter = "Task not found";
		
		public static final String exceptionCharactersInFollowingFields = "We're not handling special characters, please provide a proper value in the following fields: ";
		public static final String exceptionCharactersInSpecificField = "We're not handling special characters, please provide a proper value in the next field: ";
		
		public static final String[] ValidationStorySpecialCharacterMessage = {storyFieldStoryIdSpecialCharacter, storyFieldSprintIdSpecialCharacter, storyFieldStatusInvalidException, storyFieldAssigneSpecialCharacter};
		public static final String[] ValidationTaskSpecialCharacterMessage = {storyFieldStoryIdSpecialCharacter, storyFieldTaskIdSpecialCharacter, storyFieldStatusInvalidException, storyFieldAssigneSpecialCharacter};
}		
		
