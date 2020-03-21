package com.stories.controller;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stories.domain.StoryDomain;
import com.stories.service.StoriesServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value = "Microservices STORY", tags = "Microservices STORY")
@RequestMapping(value = "/stories")
public class StoriesController {

	@Autowired
	StoriesServiceImpl storyService;
	
	@ApiOperation(value = " GET Stories ", notes = " THIS OPERATION WILL RETURN A LIST OF STORIES ")
	@ApiResponses({ @ApiResponse(code = 200, message = " SUCCESS OPERATION ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/", produces = "application/json")
	public List<StoryDomain> getAllStories() throws Exception {
		return storyService.getAllStories();
	}

	@ApiOperation(value = " GET Story ", notes = " THIS OPERATION WILL RETURN A STORY ")
	@ApiResponses({ @ApiResponse(code = 200, message = " SUCCESS OPERATION "),
			@ApiResponse(code = 404, message = " Story not found ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = "application/json")
	public StoryDomain getStoryById(@Valid @PathVariable String id) throws Exception {
		return storyService.getStoryById(id);
	}

	@ApiOperation(value = " POST Story ", notes = " THIS OPERATION WILL ADD A STORY ")
	@ApiResponses({ @ApiResponse(code = 201, message = " SUCCESS OPERATION "),
			@ApiResponse(code = 400, message = " Story has an invalid status Json ") })
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public String createStory(@Valid @RequestBody StoryDomain request) throws Exception {
		return storyService.createStory(request);
	}

	@ApiOperation(value = " DELETE Story ", notes = " THIS OPERATION WILL DELETE A STORY ")
	@ApiResponses({ @ApiResponse(code = 204, message = " SUCCESS OPERATION "),
			@ApiResponse(code = 404, message = " Status json state is invalid\", \"The status should be: Ready to Work, Working, Testing, Ready to Accept or Accepted ") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/{id}")
	public void deleteStory(@Valid @PathVariable String id) throws Exception {
		log.info("Deleting story with id from the controller: " + id);
		storyService.deleteStory(id);
	}
	
	@ApiOperation(value = " PUT Story ", notes = " THIS OPERATION WILL UPDATE A STORY ")
	@ApiResponses({ @ApiResponse(code = 202, message = " SUCCESS OPERATION "),
			@ApiResponse(code = 404, message = " Story not found "),
			@ApiResponse(code = 400, message = " Malformed JSON request ") })
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public StoryDomain updateStory(@Valid @RequestBody StoryDomain request, @PathVariable String id) throws Exception {
		return storyService.updateStory(request, id);
	}
}