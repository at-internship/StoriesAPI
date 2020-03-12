package com.stories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;

@Slf4j
@Service
public class StoryServiceImpl implements StoryService {
	
	@Autowired
	StoriesRepository storiesRepository;
	
	@Autowired
	private MapperFacade mapperFacade;

	@Override
	public void createStory(StoryDomain request) {
		StoryModel storyModel = new StoryModel();
		storyModel = mapperFacade.map(request, StoryModel.class);
		storiesRepository.save(storyModel);
	}
	
	@Override
	public void deleteStory(String id) throws Exception {
		if (!storiesRepository.existsById(id)) {
			throw new EntityNotFoundException("Story with the given id was not found"+StoryModel.class);
		} else
			//System.err.print("No user was found for the given id.");
			storiesRepository.deleteById(id);
	}	

}
