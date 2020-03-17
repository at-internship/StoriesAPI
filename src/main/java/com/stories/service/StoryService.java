package com.stories.service;

import java.util.List;

import com.stories.domain.StoryDomain;

public interface StoryService {

	StoryDomain getStoryById(String id) throws Exception;
	
	List<StoryDomain> getAllStories() throws Exception;

	void createStory(StoryDomain request) throws Exception;

	void deleteStory(String id) throws Exception;
	
	StoryDomain updateStory(StoryDomain request, String id) throws Exception;
}