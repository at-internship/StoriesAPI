package com.stories.services;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.stories.domain.StoryDomain;

public interface StoryService {
	
	StoryDomain getStoryById(String id) throws Exception;
	
	List<StoryDomain> getAllStories() throws Exception;

}
