package com.stories.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stories.constants.StoriesApiConstants;
import com.stories.domain.StoryDomain;
import com.stories.domain.StoryDomainId;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Microservices STORY", tags = "Microservices STORY")
@RequestMapping(value = "/stories")
public class StoriesController {

	@Autowired
	StoriesServiceImpl storyService;

	@ApiOperation(value = " GET Stories ", notes = StoriesApiConstants.notesInGetStories)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/", produces = "application/json")
	@ResponseBody
	public List<StoryDomain> getAllStories() throws Exception {
		return storyService.getAllStories();
	}

	@ApiOperation(value = " GET Story ", notes = StoriesApiConstants.notesInGetStory)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseBody
	public StoryDomain getStoryById(@Valid @PathVariable String id) throws Exception {
		return storyService.getStoryById(id);
	}

	@ApiOperation(value = " POST Story ", notes = StoriesApiConstants.notesInPostStory)
	@ApiResponses({ @ApiResponse(code = 201, message = " Success operation "),
			@ApiResponse(code = 400, message = " Story has an invalid status Json "), 
			@ApiResponse(code= 409, message ="There is a story with this name already.<br>The user provided does not exist.<br>User assignee_id does not exist")})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> createStory(@Valid @RequestBody StoryDomain request) throws Exception {
		StoryDomainId storyDomainId = new StoryDomainId();
		storyDomainId.setId(storyService.createStory(request));
		return new ResponseEntity<>(storyDomainId, HttpStatus.CREATED);
	}

	@ApiOperation(value = " DELETE Story ", notes = StoriesApiConstants.notesInDeleteStory)
	@ApiResponses({ @ApiResponse(code = 204, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found ") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/{id}")
	public void deleteStory(@Valid @PathVariable String id) throws Exception {
		storyService.deleteStory(id);
	}

	@ApiOperation(value = " DELETE Task ", notes = StoriesApiConstants.notesInDeleteTask)
	@ApiResponses({ @ApiResponse(code = 204, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found ") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/{id}/tasks/{taskId}")
	public void deleteTask(@Valid @PathVariable String id, @PathVariable String taskId) throws Exception {
		storyService.deleteTask(id, taskId);
	}

	@ApiOperation(value = " PUT Story ", notes = StoriesApiConstants.notesInPutStory)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found "),
			@ApiResponse(code = 400, message = " Malformed JSON request ") })
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public StoryDomain updateStory(@Valid @RequestBody StoryDomain request, @PathVariable String id) throws Exception {
		return storyService.updateStory(request, id);
	}

	@ApiOperation(value = " PUT Task ", notes = StoriesApiConstants.notesInPutTask)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Task not found ") })
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "/{storyId}/tasks/{taskId}", produces = "application/json")
	@ResponseBody
	public TasksDomain updateTaskById(@Valid @RequestBody TasksDomain task, @PathVariable("storyId") String id,
			@PathVariable("taskId") String _id) throws Exception {
		return storyService.updateTaskById(task, id, _id);
	}

	@ApiOperation(value = " GET Tasks ", notes = StoriesApiConstants.notesInGetTasks)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{id}/tasks", produces = "application/json")
	@ResponseBody
	public List<TasksDomain> getTasksByStory(@Valid @PathVariable String id) throws EntityNotFoundException {
		return storyService.getTasksByStory(id);
	}

	@ApiOperation(value = " GET Task ", notes = StoriesApiConstants.notesInGetTask)
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Task not found ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{storyId}/tasks/{taskId}", produces = "application/json")
	@ResponseBody
	public TasksDomain getTaskById(@Valid @PathVariable("storyId") String storyId,
			@PathVariable("taskId") String taskId) throws Exception {
		return storyService.getTaskById(storyId, taskId);
	}

	@ApiOperation(value = " POST Task ", notes = StoriesApiConstants.notesInPostTask)
	@ApiResponses({ @ApiResponse(code = 201, message = " Success operation "),
			@ApiResponse(code = 400, message = " Task has an invalid status Json ")})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/{id}/tasks", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> createTask(@Valid @RequestBody TasksDomain taskDomain, @PathVariable String id) throws Exception {
		StoryDomainId storyDomainId = new StoryDomainId();
		storyDomainId.setId(storyService.createTask(taskDomain, id));
		return new ResponseEntity<>(storyDomainId, HttpStatus.CREATED);
	}

}