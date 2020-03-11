package com.stories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class StoryServiceImpl implements StoryService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StoryServiceImpl.class);
	
	@Autowired
	StoriesRepository repository;

	@Autowired
	private MapperFacade orikaMapperFacade;

	@Override
	public void createStory(com.stories.domain.StoryDomain request) {
		StoryModel storyModel = new StoryModel();
		storyModel = orikaMapperFacade.map(request, StoryModel.class);
		log.debug("Entity pre-saving - " + storyModel);
		repository.save(storyModel);
		log.debug("Entity post-saving - " + storyModel);
	}
	
	@Override
	public void deleteStory(String id) {
		if (repository.existsById(id)) {
			log.debug("Deleting user with id: " + id);
			repository.deleteById(id);
		} else
			log.error("No user was found for the given id.");
	}

	
	
}
