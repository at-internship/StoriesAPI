package com.stories.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.stories.domain.StoryDomain;
import com.stories.service.StoryServiceImpl;


@RestController
@RequestMapping(value = "/stories-qa.us-east-2.elasticbeanstalk.com/stories", produces = "application/json")
public class AppController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AppController.class);

	@Autowired
	StoryServiceImpl service;
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/createStory")
	public void createStory(@RequestBody StoryDomain storyDomain) {
		log.debug("Create user request - " + storyDomain.toString());
		service.createStory(storyDomain);
	}
	
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	@DeleteMapping("/deleteStory")
	public void deleteStory(@RequestParam(value="storyId", required=true) String storyId) {
		service.deleteStory(storyId);
    }
	
		
	
}