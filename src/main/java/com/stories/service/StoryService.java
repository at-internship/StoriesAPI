package com.stories.service;

import com.stories.domain.StoryDomain;

//Especificacion del comportamiento
public interface StoryService {
	public void createStory(StoryDomain storyDomain);
	
	void deleteStory(String id);
}
