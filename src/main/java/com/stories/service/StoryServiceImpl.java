package com.stories.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	StoriesRepository storiesRepository;

	@Autowired
	private MapperFacade mapperFacade;

	@Override
	public void createStory(StoryDomain request) throws Exception {
		StoryModel storyModel = new StoryModel();
		storyModel = mapperFacade.map(request, StoryModel.class);

		var storystatus = storyModel.getStatus();
		String[] statusArray = { "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
		boolean test = Arrays.asList(statusArray).contains(storystatus);

		if (test) {
			storiesRepository.save(storyModel);
			System.err.println("Creating story with the status indicated....");
		} else {
			System.err.println("error");
			throw new EntityNotFoundException("Status json state is invalid", StoryDomain.class);
		}

	}

	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found", StoryModel.class);
		} else
			storiesRepository.deleteById(id);
	}

	public StoryDomain updateStory(StoryDomain request, String id) throws Exception {
		StoryDomain storyDomain = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel story = mapperFacade.map(request, StoryModel.class);
		story.set_id(id);
		storiesRepository.save(story);
		storyDomain = mapperFacade.map(story, StoryDomain.class);
		return storyDomain;

	}
}