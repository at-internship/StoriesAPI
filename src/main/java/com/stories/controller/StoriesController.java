package com.stories.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.stories.domain.StoryDomain;
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

	@ApiOperation(value = " GET Stories ", notes = " This operation will return a list of stories ")
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/", produces = "application/json")
	@ResponseBody
	public List<StoryDomain> getAllStories() throws Exception {
		return storyService.getAllStories();
	}

	@ApiOperation(value = " GET Story ", notes = " This operation will return a of story ")
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found ") })
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseBody
	public StoryDomain getStoryById(@Valid @PathVariable String id) throws Exception {
		return storyService.getStoryById(id);
	}

	@ApiOperation(value = " POST Story ", notes = " This operation will add a story ")
	@ApiResponses({ @ApiResponse(code = 201, message = " Success operation "),
			@ApiResponse(code = 400, message = " Story has an invalid status Json ") })
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public String createStory(@Valid @RequestBody StoryDomain request) throws Exception {
		return storyService.createStory(request);
	}

	@ApiOperation(value = " DELETE Story ", notes = " This operation will delete a story ")
	@ApiResponses({ @ApiResponse(code = 204, message = " Success operation "),
			@ApiResponse(code = 404, message = " Status json state is invalid\", \"The status should be: Ready to Work, Working, Testing, Ready to Accept or Accepted ") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(value = "/{id}")
	public void deleteStory(@Valid @PathVariable String id) throws Exception {
		storyService.deleteStory(id);
	}

	@ApiOperation(value = " PUT Story ", notes = " This operation will update a story ")
	@ApiResponses({ @ApiResponse(code = 200, message = " Success operation "),
			@ApiResponse(code = 404, message = " Story not found "),
			@ApiResponse(code = 400, message = " Malformed JSON request ") })
	@ResponseStatus(value = HttpStatus.OK)
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public StoryDomain updateStory(@Valid @RequestBody StoryDomain request, @PathVariable String id) throws Exception {
		return storyService.updateStory(request, id);
	}
}