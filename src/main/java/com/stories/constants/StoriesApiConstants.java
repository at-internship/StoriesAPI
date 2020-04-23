package com.stories.constants;

public class StoriesApiConstants {
		public static final String[] statusArray = { "Refining", "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		public static final String[] storyDomainValidation = { "Story_id", "Sprint_id", "Status", "Priority", "Assignee_id" };
		public static final String[] taskDomainValidation = { "Story_id", "Task_id", "Status", "assignee" };
		public static final int[] pointsArray = { 0, 1, 2, 3, 5 };
		public static final String[] specialCharacters = {"?","!","(",")","$","&","/","@","^",">","<","|","#",";",":","[","]","{","}","+","*","¨","¿","-","=","%",",","'",".","_","°","~"};
		public static final String[] validationPath = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };
		
		public static final String badRequest = "BAD_REQUEST";
		public static final String conflict = "CONFLICT";
		public static final String pathStories = "/stories/";
		public static final String pathTasks = "/tasks/";
		public static final String validationRespons = "";
		
		
		public static final String exceptionStatusfield = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.";
		public static final String exceptionNameAndStatusfielsdRequired = "The JSON format provided is invalid, please provide the required fields ('Name','Status').";
		public static final String exceptionNamefieldRequired = "The JSON format provided is invalid, please provide the required field ('Name').";
		public static final String exceptionNamefieldExist = "There is a story with this name already.";
		public static final String exceptionStatusfieldRequired = "The JSON format provided is invalid, please provide the required field ('Status').";
		public static final String exceptionTaskEmpty = "There are not tasks for this user story yet.";
		public static final String exceptionNumbererror = "400";
		public static final String exceptionUser = "The user provided does not exist";
		public static final String exceptionStories = "Stories not found";
		public static final String exceptionStory = "Story with the given id was not found.";
		public static final String exceptionTask = "Task with the given id was not found.";
		public static final String exceptionStorySprintId = "The id entered in the sprint_id field does not exist";
		public static final String exceptionStoryAssigne = "User assignee_id does not exist";
		public static final String exceptionProgressFieldNegative = "The number entered in the progress field is a negative number";
		public static final String exceptionProgressFieldExceeds = "The number entered in the progress field exceeds 100%";
		public static final String exceptionPointsFieldNegative = "The number entered in the points field is a negative number";
		public static final String exceptionPointsFieldInvalid = "The number entered in the points field does not match a valid story point";
		public static final String exceptionCharactersInFollowingFields = "We're not handling special characters, please provide a proper value in the following fields: ";
		public static final String exceptionCharactersInNextField = "We're not handling special characters, please provide a proper value in the next field: ";
		
}		
		
