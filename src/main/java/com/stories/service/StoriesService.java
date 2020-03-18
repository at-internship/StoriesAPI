package com.stories.service;

import com.stories.domain.StoryDomain;

import java.util.List;

public interface StoriesService {

	StoryDomain getStoryById(String id) throws Exception;

	List<StoryDomain> getAllStories() throws Exception;

	String createStory(StoryDomain request) throws Exception;
	
	void deleteStory(String id) throws Exception;
	
	StoryDomain updateStory(StoryDomain request, String id) throws Exception;
}