package com.stories.services;

import com.stories.domain.StoryDomain;

public interface StoryService {

	public StoryDomain updateStory(StoryDomain request, String id);

}
