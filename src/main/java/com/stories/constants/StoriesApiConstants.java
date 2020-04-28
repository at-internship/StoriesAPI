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
		
		public static final String storyFieldStatusInvalidException = "The Status field should be one of the following options: 'Refining', 'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'; but none found";
		public static final String storyFieldsNameAndStatusRequiredException = "The JSON format provided is invalid, please provide the required fields 'Name','Status'";
		public static final String storyFieldNameRequiredException = "The JSON format provided is invalid, please provide the required field 'Name'";
		public static final String storyFieldStatusRequiredException = "The JSON format provided is invalid, please provide the required field 'Status'";
		public static final String storyFieldNameExistException = "There is a story with this name already";
		public static final String storyFieldTasksEmptyException = "There are not tasks for this user story yet";
		public static final String storiesNotFoundException = "Stories not found";
		public static final String storyFieldIdNotFoundException = "Story not found";
		public static final String storyFieldSprintIdDoesntExistException = "The id entered in the sprint_id field does not exist";
		public static final String storyFieldAssigneDoesntExistException = "User assignee_id does not exist";
		public static final String storyFieldStoryIdSpecialCharacter = "Story not found";
		public static final String storyFieldSprintIdSpecialCharacter = "The id entered in the sprint_id field does not exist";
		public static final String storyFieldAssigneSpecialCharacter = "User assignee_id does not exist";
		public static final String storyFieldProgressNegativeException = "The number entered in the progress field is a negative number";
		public static final String storyFieldProgressExceedsException = "The number entered in the progress field exceeds 100%";
		public static final String storyFieldPointsNegativeException = "The number entered in the points field is a negative number";
		public static final String storyFieldPointsInvalidException  = "The number entered in the points field does not match a valid story point";
		
		public static final String taskFieldAssigneeNotFoundException = "User assignee_id does not exist";
		public static final String taskFieldIdNotFoundException = "Task not found";
		public static final String taskFieldNameRequiredException = "The JSON format provided is invalid, please provide the required field 'Name'";
		public static final String storyFieldTaskIdSpecialCharacter = "Task not found";
		
		public static final String[] ValidationStorySpecialCharacterMessage = {storyFieldStoryIdSpecialCharacter, storyFieldSprintIdSpecialCharacter, storyFieldStatusInvalidException, storyFieldAssigneSpecialCharacter};
		public static final String[] ValidationTaskSpecialCharacterMessage = {storyFieldStoryIdSpecialCharacter, storyFieldTaskIdSpecialCharacter, storyFieldStatusInvalidException, storyFieldAssigneSpecialCharacter};
		
		public static final String notesInGetStories = "<b> This method will return a list of users stories. </b>";
		public static final String notesInGetStory = "<b> This method will return a of story. </b>";
		public static final String notesInPostStory = "<b> This method will create a new user story. </b><br>"
										+ "<dl> <dt><ul> <b><li> Fields required : </li></b> </ul></dt> "
										+ "<dd><ol> 		<li> Name </li> "
										+ 		   			"<li> Status </li> "
										+ "</ol></dd> </dl><br>"
										+"<mark>Please don't use an <b>ID</b> field in your request body to create a new Story.</mark>";
		public static final String notesInDeleteStory = "<b> This method will delete a user story. </b>";
		public static final String notesInPutStory = "<b> This method will update a user story. </b><br>"
										+"<mark>Please don't use an <b>ID</b> field in your request body to edit a Story.</mark>";
		public static final String notesInDeleteTask = "<b> This method will delete a task. </b>";
		public static final String notesInPutTask = "<b> This method will update a task from a story. </b><br>"
										+"<mark>Please don't use an <b>ID</b> field in your request body to edit a Task.</mark>";;
		public static final String notesInGetTasks = "<b> This method will return a list of the tasks of a story. </b>";
		public static final String notesInGetTask = "<b> This method will return a task from a story. </b>";
		public static final String notesInPostTask = "<b> This method will create a new task. </b><br>"
										+ "<dl> <dt><ul> <b><li>Fields required :</li></b> </ul></dt> "
										+ "<dd><ol> 	   	<li>Name</li> </ol></dd> </dl> <br>"
										+ "<mark>Please don't use an <b>ID</b> field in your request body to create a new Task.</mark>";
}		
		
