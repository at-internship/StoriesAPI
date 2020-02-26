package com.stories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class StoryServiceImpl {

	@Autowired
	StoriesRepository storiesRepository;

	@Autowired
	private MapperFacade mapperFacate;

	public StoryDomain updateStory(StoryDomain request, String id) throws Exception {
		StoryDomain storyDomain = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel story = mapperFacate.map(request, StoryModel.class);
		story.set_id(id);
		storiesRepository.save(story);
		storyDomain = mapperFacate.map(story, StoryDomain.class);
		return storyDomain;

	}
}
