package com.stories.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private MapperFacade orikaMapperFacade;

	@Autowired
	private StoriesRepository storiesRepository;

	@Override
	public StoryDomain getStoryById(String id) throws Exception{
		StoryDomain story = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel storyModel = storiesRepository.findById(id).get();
		story = orikaMapperFacade.map(storyModel, StoryDomain.class);
		return story;
	}
	
	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> story = new ArrayList<StoryModel>();
		story = storiesRepository.findAll();
		List<StoryDomain> stories = new ArrayList<>();
		if(story == null)
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		for (int i = 0; i < story.size(); i++) {
			stories.add(orikaMapperFacade.map(story.get(i), StoryDomain.class));
		}
		return stories;
	}
}