package com.stories.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stories.domain.StoryDomain;
import com.stories.services.StoryServiceImpl;

@RestController
@RequestMapping(value = "/stories", produces = "application/json")
public class StoriesController {
	
	@Autowired
	private StoryServiceImpl storyService;
	
	@GetMapping(value = "/", produces = "application/json")
	public List<StoryDomain> getAllStories() throws Exception{
		return storyService.getAllStories();
	}	
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping(value = "/{id}", produces = "application/json")
	public StoryDomain getStoryById(@Valid @PathVariable String id) throws Exception{
		return storyService.getStoryById(id);
	}
	
}
