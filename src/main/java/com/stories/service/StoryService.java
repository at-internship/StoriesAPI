package com.stories.service;

import com.stories.domain.StoryDomain;

public interface StoryService {
	void createStory(StoryDomain request) throws Exception;

	void deleteStory(String id) throws Exception;
}