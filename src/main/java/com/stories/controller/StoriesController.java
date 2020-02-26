package com.stories.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stories.domain.StoryDomain;
import com.stories.services.StoryServiceImpl;

@RestController
@RequestMapping(value = "/stories", produces = "application/json")
public class StoriesController {

	@Autowired
	StoryServiceImpl storyService;

	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public StoryDomain updateStory(@Valid @RequestBody StoryDomain request, @PathVariable String id) throws Exception {
		return storyService.updateStory(request, id);
	}

}
