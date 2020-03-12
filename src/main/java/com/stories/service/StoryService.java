package com.stories.service;

import com.stories.domain.StoryDomain;

public interface StoryService {
	void createStory(StoryDomain request);
	
	void deleteStory(String id) throws Exception;
}
