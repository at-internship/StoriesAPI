package com.stories.service;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StoriesServiceImpl implements StoriesService {

	@Autowired
	StoriesRepository storiesRepository;

	@Autowired
	private MapperFacade mapperFacade;

	@Override
	public void createStory(StoryDomain request) throws Exception {
		StoryModel storyModel = new StoryModel();
		storyModel = mapperFacade.map(request, StoryModel.class);

		String storystatus = storyModel.getStatus();
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
		StoryDomain storyDomain =  mapperFacade.map(request, StoryDomain.class);
		StoryModel story = mapperFacade.map(storyDomain, StoryModel.class);
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		story.set_id(id);
		storiesRepository.save(story);
		storyDomain = mapperFacade.map(story, StoryDomain.class);
		return storyDomain;

	}

	@Override
	public StoryDomain getStoryById(String id) throws Exception {
		StoryDomain story = new StoryDomain();
		if (!storiesRepository.existsById(id))
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		StoryModel storyModel = storiesRepository.findById(id).get();
		story = mapperFacade.map(storyModel, StoryDomain.class);
		return story;
	}

	@Override
	public List<StoryDomain> getAllStories() throws Exception {
		List<StoryModel> story = new ArrayList<StoryModel>();
		story = storiesRepository.findAll();
		List<StoryDomain> stories = new ArrayList<>();
		if (story == null)
			throw new EntityNotFoundException("Story not found", StoryDomain.class);
		for (int i = 0; i < story.size(); i++) {
			stories.add(mapperFacade.map(story.get(i), StoryDomain.class));
		}
		return stories;
	}
}